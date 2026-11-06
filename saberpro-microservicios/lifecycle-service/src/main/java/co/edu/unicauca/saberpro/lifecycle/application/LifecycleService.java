/**
 * @file LifecycleService.java
 * @brief Casos de uso de Lifecycle Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.application;

import co.edu.unicauca.saberpro.lifecycle.application.dto.QuestionStatusChanged;
import co.edu.unicauca.saberpro.lifecycle.application.dto.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.lifecycle.application.port.StatusChangePublisher;
import co.edu.unicauca.saberpro.lifecycle.domain.exception.InvalidStateTransitionException;
import co.edu.unicauca.saberpro.lifecycle.domain.exception.NotFoundException;
import co.edu.unicauca.saberpro.lifecycle.domain.model.OperationOutcome;
import co.edu.unicauca.saberpro.lifecycle.domain.model.ProcessedOperation;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.lifecycle.domain.port.LifecycleRepository;
import co.edu.unicauca.saberpro.lifecycle.domain.port.OperationLogRepository;

import java.time.Instant;
import java.util.Optional;

/**
 * @brief Procesa el comando SubmitQuestionForReview (Punto 2.2).
 *
 * Java puro; Spring lo registra en {@code ApplicationConfig}.
 */
public class LifecycleService {

    private final LifecycleRepository lifecycles;
    private final OperationLogRepository operations;
    private final StatusChangePublisher publisher;

    /**
     * @brief Inyecta las dependencias.
     * @param lifecycles Persistencia de ciclos de vida.
     * @param operations Registro de operaciones.
     * @param publisher  Publicador de eventos.
     */
    public LifecycleService(LifecycleRepository lifecycles, OperationLogRepository operations,
                            StatusChangePublisher publisher) {
        this.lifecycles = lifecycles;
        this.operations = operations;
        this.publisher = publisher;
    }

    /**
     * @brief Aplica la transición DRAFT → PENDING_REVIEW.
     *
     * <ol>
     *   <li>Si el operationId ya se procesó, no hace nada (idempotencia ante
     *       reentregas de RabbitMQ).</li>
     *   <li>Carga el ciclo de vida o lo crea en DRAFT.</li>
     *   <li>Delega en el estado actual (patrón State).</li>
     *   <li>Guarda, publica el evento y registra la operación.</li>
     * </ol>
     *
     * @param command Comando recibido.
     * @return Registro de la operación, o vacío si era un duplicado.
     */
    public Optional<ProcessedOperation> handle(SubmitQuestionForReview command) {
        if (operations.exists(command.operationId())) {
            return Optional.empty();
        }
        QuestionLifecycle lifecycle = lifecycles.findByQuestionId(command.questionId())
                .orElseGet(() -> QuestionLifecycle.startAsDraft(command.questionId()));
        QuestionStatus from = lifecycle.getStatus();
        ProcessedOperation result;
        try {
            lifecycle.submitForReview(command.operationId());
            lifecycles.save(lifecycle);
            publisher.publish(new QuestionStatusChanged(command.operationId(), command.questionId(),
                    lifecycle.getStatus().name(), lifecycle.getUpdatedAt()));
            result = new ProcessedOperation(command.operationId(), command.questionId(), OperationOutcome.APPLIED,
                    from + " -> " + lifecycle.getStatus(), Instant.now());
        } catch (InvalidStateTransitionException e) {
            result = new ProcessedOperation(command.operationId(), command.questionId(), OperationOutcome.REJECTED,
                    e.getMessage(), Instant.now());
        }
        operations.save(result);
        return Optional.of(result);
    }

    /**
     * @brief Consulta el ciclo de vida de una pregunta.
     * @param questionId Pregunta.
     * @return Ciclo de vida.
     * @throws NotFoundException si no se ha procesado ningún comando para ella.
     */
    public QuestionLifecycle getLifecycle(Long questionId) {
        return lifecycles.findByQuestionId(questionId)
                .orElseThrow(() -> new NotFoundException("Ciclo de vida de la pregunta", questionId));
    }

    /**
     * @brief Consulta una operación.
     * @param operationId Id devuelto en el 202 Accepted.
     * @return Operación procesada.
     * @throws NotFoundException si aún no se ha procesado.
     */
    public ProcessedOperation getOperation(String operationId) {
        return operations.findById(operationId)
                .orElseThrow(() -> new NotFoundException("Operación", operationId));
    }
}
