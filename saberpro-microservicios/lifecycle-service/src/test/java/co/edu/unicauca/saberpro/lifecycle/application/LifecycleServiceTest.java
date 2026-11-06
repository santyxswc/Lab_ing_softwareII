/**
 * @file LifecycleServiceTest.java
 * @brief Pruebas del procesamiento del comando SubmitQuestionForReview.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.application;

import co.edu.unicauca.saberpro.lifecycle.application.dto.QuestionStatusChanged;
import co.edu.unicauca.saberpro.lifecycle.application.dto.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.lifecycle.domain.model.OperationOutcome;
import co.edu.unicauca.saberpro.lifecycle.domain.model.ProcessedOperation;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.lifecycle.domain.port.LifecycleRepository;
import co.edu.unicauca.saberpro.lifecycle.domain.port.OperationLogRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @brief Usa repositorios en memoria y un publicador que captura eventos.
 */
class LifecycleServiceTest {

    /** @brief Implementa ambos puertos en memoria. */
    static class InMemory implements LifecycleRepository, OperationLogRepository {
        final Map<Long, QuestionLifecycle> lifecycles = new HashMap<>();
        final Map<String, ProcessedOperation> ops = new HashMap<>();

        @Override
        public QuestionLifecycle save(QuestionLifecycle lc) {
            lifecycles.put(lc.getQuestionId(), lc);
            return lc;
        }

        @Override
        public Optional<QuestionLifecycle> findByQuestionId(Long id) {
            return Optional.ofNullable(lifecycles.get(id));
        }

        @Override
        public void save(ProcessedOperation op) {
            ops.put(op.operationId(), op);
        }

        @Override
        public Optional<ProcessedOperation> findById(String id) {
            return Optional.ofNullable(ops.get(id));
        }

        @Override
        public boolean exists(String id) {
            return ops.containsKey(id);
        }
    }

    private final InMemory store = new InMemory();
    private final List<QuestionStatusChanged> events = new ArrayList<>();
    private final LifecycleService service = new LifecycleService(store, store, events::add);

    private static SubmitQuestionForReview cmd(String op) {
        return new SubmitQuestionForReview(op, 7L, Instant.now());
    }

    /** @brief Primer envío: DRAFT → PENDING_REVIEW y se publica el evento. */
    @Test
    void appliesTransitionAndPublishes() {
        ProcessedOperation op = service.handle(cmd("op-1")).orElseThrow();
        assertEquals(OperationOutcome.APPLIED, op.outcome());
        assertEquals(QuestionStatus.PENDING_REVIEW, service.getLifecycle(7L).getStatus());
        assertEquals(1, events.size());
        assertEquals("PENDING_REVIEW", events.get(0).newStatus());
    }

    /** @brief Segundo envío con otro operationId: REJECTED y sin evento nuevo. */
    @Test
    void rejectsSecondSubmit() {
        service.handle(cmd("op-1"));
        ProcessedOperation op = service.handle(cmd("op-2")).orElseThrow();
        assertEquals(OperationOutcome.REJECTED, op.outcome());
        assertEquals(1, events.size());
    }

    /** @brief Mismo operationId reentregado: se ignora (idempotencia). */
    @Test
    void ignoresDuplicateDelivery() {
        service.handle(cmd("op-1"));
        assertTrue(service.handle(cmd("op-1")).isEmpty());
        assertEquals(1, events.size());
    }
}
