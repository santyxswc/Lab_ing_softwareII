/**
 * @file QuestionServiceTest.java
 * @brief Pruebas de los casos de uso con dobles de prueba en memoria.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application;

import co.edu.unicauca.saberpro.question.application.dto.CreateQuestionCommand;
import co.edu.unicauca.saberpro.question.application.dto.SubmissionReceipt;
import co.edu.unicauca.saberpro.question.application.dto.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.question.application.exception.ValidationRejectedException;
import co.edu.unicauca.saberpro.question.application.port.JustificationValidator;
import co.edu.unicauca.saberpro.question.domain.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.question.domain.exception.QuestionNotFoundException;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.question.domain.port.QuestionRepository;
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
 * @brief Verifica el flujo crear / enviar a revisión sin Spring ni red.
 */
class QuestionServiceTest {

    /** @brief Repositorio en memoria que asigna ids incrementales. */
    static class InMemoryRepository implements QuestionRepository {
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
    private final JustificationValidator lengthValidator =
            j -> new JustificationValidator.Outcome(j.length() > 15, "regla de prueba");
    private final QuestionService service =
            new QuestionService(QuestionBuilder::new, lengthValidator, repository, published::add);

    private static CreateQuestionCommand command(String justification) {
        return new CreateQuestionCommand("¿2 + 2?", justification,
                List.of(new CreateQuestionCommand.OptionData("A", "3"),
                        new CreateQuestionCommand.OptionData("B", "4"),
                        new CreateQuestionCommand.OptionData("C", "5"),
                        new CreateQuestionCommand.OptionData("D", "6")),
                "B");
    }

    /** @brief Justificación válida → se guarda en DRAFT. */
    @Test
    void createsWhenJustificationIsValid() {
        Question q = service.create(command("Dos más dos es igual a cuatro."));
        assertNotNull(q.getId());
        assertEquals(QuestionStatus.DRAFT, q.getStatus());
        assertEquals(1, repository.data.size());
    }

    /** @brief Justificación rechazada → excepción y NO se guarda. */
    @Test
    void doesNotSaveWhenJustificationIsRejected() {
        assertThrows(ValidationRejectedException.class, () -> service.create(command("Muy corta")));
        assertTrue(repository.data.isEmpty());
    }

    /** @brief Enviar a revisión publica el comando con un operationId. */
    @Test
    void submitPublishesCommand() {
        Question q = service.create(command("Dos más dos es igual a cuatro."));
        SubmissionReceipt receipt = service.submitForReview(q.getId());
        assertEquals(1, published.size());
        assertEquals(receipt.operationId(), published.get(0).operationId());
        assertEquals(q.getId(), published.get(0).questionId());
    }

    /** @brief Enviar una pregunta inexistente falla y no publica nada. */
    @Test
    void submitUnknownQuestionFails() {
        assertThrows(QuestionNotFoundException.class, () -> service.submitForReview(99L));
        assertTrue(published.isEmpty());
    }

    /** @brief El evento de Lifecycle actualiza el estado guardado. */
    @Test
    void syncStatusUpdatesQuestion() {
        Question q = service.create(command("Dos más dos es igual a cuatro."));
        service.syncStatus(q.getId(), QuestionStatus.PENDING_REVIEW);
        assertEquals(QuestionStatus.PENDING_REVIEW, service.getById(q.getId()).getStatus());
    }
}
