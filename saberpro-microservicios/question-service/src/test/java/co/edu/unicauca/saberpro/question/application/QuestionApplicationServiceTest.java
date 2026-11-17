/**
 * @file QuestionApplicationServiceTest.java
 * @brief Pruebas de los casos de uso con dobles de prueba en memoria.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application;

import co.edu.unicauca.saberpro.question.application.dto.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.question.application.exception.ValidationRejectedException;
import co.edu.unicauca.saberpro.question.application.port.JustificationValidator;
import co.edu.unicauca.saberpro.question.domain.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.question.domain.exception.QuestionNotFoundException;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.question.domain.ports.inbound.CrearPreguntaCommand;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.NotificacionPort;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @brief Verifica el flujo crear / enviar a evaluación sin Spring ni red.
 */
class QuestionApplicationServiceTest {

    /** @brief Adaptador de persistencia en memoria que asigna ids incrementales. */
    static class InMemoryRepository implements PreguntaRepositoryPort {
        final Map<Long, Question> data = new HashMap<>();
        long seq = 0;

        @Override
        public Question save(Question q) {
            Long id = q.getId() == null ? ++seq : q.getId();
            Question stored = Question.restore(id, q.getStatement(), q.getJustification(), q.getOptions(),
                    q.getStatus(), q.getCreatedAt());
            data.put(id, stored);
            return stored;
        }

        @Override
        public Optional<Question> findById(Long id) {
            return Optional.ofNullable(data.get(id));
        }

        @Override
        public List<Question> findAll() {
            return List.copyOf(data.values());
        }
    }

    private final InMemoryRepository repository = new InMemoryRepository();
    private final List<SubmitQuestionForReview> published = new ArrayList<>();
    /** Doble de prueba del puerto de salida NotificacionPort (RabbitMQ). */
    private final NotificacionPort notificacionPort =
            (operationId, questionId, solicitadoEn) ->
                    published.add(new SubmitQuestionForReview(operationId, questionId, solicitadoEn));
    /** Doble de prueba de Validation Service: rechaza justificaciones con la marca. */
    private final JustificationValidator validator = j -> j.contains("RECHAZADA")
            ? new JustificationValidator.Outcome(false, "regla de prueba")
            : new JustificationValidator.Outcome(true, "ok");
    private final QuestionApplicationService service =
            new QuestionApplicationService(QuestionBuilder::new, validator, repository, notificacionPort);

    private static CrearPreguntaCommand command(String justification) {
        return new CrearPreguntaCommand("¿2 + 2?", justification,
                List.of(new CrearPreguntaCommand.OptionData("A", "3"),
                        new CrearPreguntaCommand.OptionData("B", "4"),
                        new CrearPreguntaCommand.OptionData("C", "5"),
                        new CrearPreguntaCommand.OptionData("D", "6")),
                "B");
    }

    /** @brief Justificación válida → se guarda en DRAFT. */
    @Test
    void createsWhenJustificationIsValid() {
        Question q = service.crearPregunta(command("Dos más dos es igual a cuatro."));
        assertNotNull(q.getId());
        assertEquals(QuestionStatus.DRAFT, q.getStatus());
        assertEquals(1, repository.data.size());
    }

    /** @brief Justificación rechazada → excepción y NO se guarda. */
    @Test
    void doesNotSaveWhenJustificationIsRejected() {
        assertThrows(ValidationRejectedException.class,
                () -> service.crearPregunta(command("Justificación RECHAZADA por el servicio.")));
        assertTrue(repository.data.isEmpty());
    }

    /** @brief Enviar a evaluación notifica el comando con un operationId. */
    @Test
    void submitPublishesCommand() {
        Question q = service.crearPregunta(command("Dos más dos es igual a cuatro."));
        String operationId = service.enviarAEvaluacion(q.getId());
        assertEquals(1, published.size());
        assertEquals(operationId, published.get(0).operationId());
        assertEquals(q.getId(), published.get(0).questionId());
    }

    /** @brief Enviar una pregunta inexistente falla y no notifica nada. */
    @Test
    void submitUnknownQuestionFails() {
        assertThrows(QuestionNotFoundException.class, () -> service.enviarAEvaluacion(99L));
        assertTrue(published.isEmpty());
    }

    /** @brief El evento de Lifecycle actualiza el estado guardado. */
    @Test
    void syncStatusUpdatesQuestion() {
        Question q = service.crearPregunta(command("Dos más dos es igual a cuatro."));
        service.syncStatus(q.getId(), QuestionStatus.PENDING_REVIEW);
        assertEquals(QuestionStatus.PENDING_REVIEW, service.getById(q.getId()).getStatus());
    }
}
