/**
 * @file QuestionApplicationService.java
 * @brief Casos de uso de Question Service (implementa los puertos de entrada).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application;

import co.edu.unicauca.saberpro.question.application.exception.ValidationRejectedException;
import co.edu.unicauca.saberpro.question.application.port.JustificationValidator;
import co.edu.unicauca.saberpro.question.domain.builder.IQuestionBuilder;
import co.edu.unicauca.saberpro.question.domain.exception.QuestionNotFoundException;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.question.domain.ports.inbound.CrearPreguntaCommand;
import co.edu.unicauca.saberpro.question.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.question.domain.ports.inbound.EvaluarPreguntaUseCase;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.NotificacionPort;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @brief Orquesta Builder, validación remota, persistencia y mensajería.
 *
 * Java puro: depende solo de puertos (interfaces) del dominio. Implementa los
 * puertos de entrada {@link CrearPreguntaUseCase} y {@link EvaluarPreguntaUseCase}.
 * Spring lo registra como bean en {@code ApplicationConfig}.
 */
public class QuestionApplicationService implements CrearPreguntaUseCase, EvaluarPreguntaUseCase {

    private final Supplier<IQuestionBuilder> builderFactory;
    private final JustificationValidator validator;
    private final PreguntaRepositoryPort repository;
    private final NotificacionPort notificacionPort;

    /**
     * @brief Inyecta las dependencias.
     * @param builderFactory   Crea un builder nuevo por cada pregunta.
     * @param validator        Cliente de Validation Service.
     * @param repository       Puerto de persistencia de preguntas.
     * @param notificacionPort Puerto de notificación (RabbitMQ).
     */
    public QuestionApplicationService(Supplier<IQuestionBuilder> builderFactory, JustificationValidator validator,
                           PreguntaRepositoryPort repository, NotificacionPort notificacionPort) {
        this.builderFactory = builderFactory;
        this.validator = validator;
        this.repository = repository;
        this.notificacionPort = notificacionPort;
    }

    /**
     * @brief Crea una pregunta (Punto 2.1).
     *
     * Pasos: 1) el Builder y los Objetos de Valor validan la estructura y la
     * justificación; 2) se llama de forma síncrona a Validation Service; 3) solo
     * si es válida se guarda en DRAFT.
     *
     * @param command Datos de la pregunta.
     * @return Pregunta guardada con id.
     * @throws co.edu.unicauca.saberpro.question.domain.exception.ReglaNegocioException
     *         si el enunciado o la justificación violan una invariante de dominio.
     * @throws co.edu.unicauca.saberpro.question.domain.exception.InvalidQuestionException
     *         si la estructura es inválida.
     * @throws ValidationRejectedException si la justificación es rechazada.
     */
    @Override
    public Question crearPregunta(CrearPreguntaCommand command) {
        IQuestionBuilder builder = builderFactory.get()
                .withStatement(command.statement())
                .withJustification(command.justification())
                .withCorrectAnswer(command.correctAnswer());
        List<CrearPreguntaCommand.OptionData> options =
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
     * @brief Envía una pregunta a evaluación (revisión) de forma asíncrona (Punto 2.2).
     *
     * Notifica la solicitud y retorna enseguida; Lifecycle Service hace la
     * transición cuando consume el mensaje.
     *
     * @param questionId Pregunta a enviar.
     * @return El operationId de la solicitud.
     * @throws QuestionNotFoundException si la pregunta no existe.
     */
    @Override
    public String enviarAEvaluacion(Long questionId) {
        Question question = getById(questionId);
        String operationId = UUID.randomUUID().toString();
        notificacionPort.notificarRevision(operationId, question.getId(), Instant.now());
        return operationId;
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
