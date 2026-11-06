/**
 * @file QuestionService.java
 * @brief Casos de uso de Question Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application;

import co.edu.unicauca.saberpro.question.application.dto.CreateQuestionCommand;
import co.edu.unicauca.saberpro.question.application.dto.SubmissionReceipt;
import co.edu.unicauca.saberpro.question.application.dto.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.question.application.exception.ValidationRejectedException;
import co.edu.unicauca.saberpro.question.application.port.JustificationValidator;
import co.edu.unicauca.saberpro.question.application.port.ReviewCommandPublisher;
import co.edu.unicauca.saberpro.question.domain.builder.IQuestionBuilder;
import co.edu.unicauca.saberpro.question.domain.exception.QuestionNotFoundException;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.question.domain.port.QuestionRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @brief Orquesta Builder, validación remota, persistencia y mensajería.
 *
 * Java puro: depende solo de puertos (interfaces). Spring lo registra como
 * bean en {@code ApplicationConfig}.
 */
public class QuestionService {

    private final Supplier<IQuestionBuilder> builderFactory;
    private final JustificationValidator validator;
    private final QuestionRepository repository;
    private final ReviewCommandPublisher publisher;

    /**
     * @brief Inyecta las dependencias.
     * @param builderFactory Crea un builder nuevo por cada pregunta.
     * @param validator      Cliente de Validation Service.
     * @param repository     Persistencia de preguntas.
     * @param publisher      Publicador AMQP.
     */
    public QuestionService(Supplier<IQuestionBuilder> builderFactory, JustificationValidator validator,
                           QuestionRepository repository, ReviewCommandPublisher publisher) {
        this.builderFactory = builderFactory;
        this.validator = validator;
        this.repository = repository;
        this.publisher = publisher;
    }

    /**
     * @brief Crea una pregunta (Punto 2.1).
     *
     * Pasos: 1) el Builder valida la estructura; 2) se llama de forma
     * síncrona a Validation Service; 3) solo si es válida se guarda en DRAFT.
     *
     * @param command Datos de la pregunta.
     * @return Pregunta guardada con id.
     * @throws co.edu.unicauca.saberpro.question.domain.exception.InvalidQuestionException
     *         si la estructura es inválida.
     * @throws ValidationRejectedException si la justificación es rechazada.
     */
    public Question create(CreateQuestionCommand command) {
        IQuestionBuilder builder = builderFactory.get()
                .withStatement(command.statement())
                .withJustification(command.justification())
                .withCorrectAnswer(command.correctAnswer());
        List<CreateQuestionCommand.OptionData> options =
                command.options() == null ? List.of() : command.options();
        options.forEach(o -> builder.withOption(o.label(), o.text()));
        Question draft = builder.build();

        JustificationValidator.Outcome outcome = validator.validate(draft.getJustification());
        if (!outcome.valid()) {
            throw new ValidationRejectedException(outcome.message());
        }
        return repository.save(draft);
    }

    /**
     * @brief Envía una pregunta a revisión de forma asíncrona (Punto 2.2).
     *
     * Publica el comando y retorna enseguida; Lifecycle Service hace la
     * transición cuando consume el mensaje.
     *
     * @param questionId Pregunta a enviar.
     * @return Comprobante con el operationId.
     * @throws QuestionNotFoundException si la pregunta no existe.
     */
    public SubmissionReceipt submitForReview(Long questionId) {
        Question question = getById(questionId);
        String operationId = UUID.randomUUID().toString();
        publisher.publish(new SubmitQuestionForReview(operationId, question.getId(), Instant.now()));
        return new SubmissionReceipt(operationId, question.getId());
    }

    /**
     * @brief Replica el estado que calculó Lifecycle Service.
     * @param questionId Pregunta afectada.
     * @param newStatus  Nuevo estado.
     * @throws QuestionNotFoundException si la pregunta no existe.
     */
    public void syncStatus(Long questionId, QuestionStatus newStatus) {
        Question question = getById(questionId);
        question.syncStatus(newStatus);
        repository.save(question);
    }

    /**
     * @brief Busca una pregunta.
     * @param id Identificador.
     * @return La pregunta.
     * @throws QuestionNotFoundException si no existe.
     */
    public Question getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }

    /**
     * @brief Lista todas las preguntas.
     * @return Preguntas guardadas.
     */
    public List<Question> findAll() {
        return repository.findAll();
    }
}
