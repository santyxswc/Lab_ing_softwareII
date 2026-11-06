/**
 * @file Question.java
 * @brief Modelo de dominio de una pregunta (reactivo) de selección múltiple.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @brief Reactivo del Banco de Preguntas Saber PRO.
 *
 * Clase de negocio en Java puro (Punto 3.1): no tiene {@code @Entity},
 * {@code @Table} ni {@code @Id}. La persistencia vive en
 * {@code infrastructure.jpa.record.QuestionRecord} y se traduce con
 * {@code QuestionDomainMapper}.
 *
 * Las preguntas nuevas se crean con {@code QuestionBuilder}, que valida la
 * estructura; las ya guardadas se reconstruyen con {@link #restore}.
 */
public final class Question {

    private final Long id;
    private final String statement;
    private final String justification;
    private final List<AnswerOption> options;
    private QuestionStatus status;
    private final Instant createdAt;

    private Question(Long id, String statement, String justification,
                     List<AnswerOption> options, QuestionStatus status, Instant createdAt) {
        this.id = id;
        this.statement = Objects.requireNonNull(statement, "statement");
        this.justification = Objects.requireNonNull(justification, "justification");
        this.options = List.copyOf(options);
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    /**
     * @brief Crea una pregunta nueva en estado {@link QuestionStatus#DRAFT}.
     *
     * Solo debe llamarla {@code QuestionBuilder} después de validar.
     *
     * @param statement     Enunciado.
     * @param justification Justificación de la respuesta correcta.
     * @param options       Las cuatro opciones de respuesta.
     * @return Pregunta sin id (aún no persistida).
     */
    public static Question newDraft(String statement, String justification, List<AnswerOption> options) {
        return new Question(null, statement, justification, options, QuestionStatus.DRAFT, Instant.now());
    }

    /**
     * @brief Reconstruye una pregunta ya persistida (la usa el mapeador).
     * @param id            Identificador en base de datos.
     * @param statement     Enunciado.
     * @param justification Justificación.
     * @param options       Opciones de respuesta.
     * @param status        Estado guardado.
     * @param createdAt     Fecha de creación.
     * @return Pregunta de dominio.
     */
    public static Question restore(Long id, String statement, String justification,
                                   List<AnswerOption> options, QuestionStatus status, Instant createdAt) {
        return new Question(id, statement, justification, options, status, createdAt);
    }

    /**
     * @brief Registra el estado que Lifecycle Service calculó.
     *
     * No decide la transición: la regla vive en Lifecycle Service (patrón
     * State). Aquí solo se replica el resultado para poder consultarlo.
     *
     * @param newStatus Estado notificado.
     */
    public void syncStatus(QuestionStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "newStatus");
    }

    /**
     * @brief Devuelve la opción marcada como correcta.
     * @return La opción correcta.
     * @throws IllegalStateException si ninguna lo está (no debería pasar
     *         con preguntas construidas por el Builder).
     */
    public AnswerOption getCorrectOption() {
        return options.stream()
                .filter(AnswerOption::isCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("La pregunta no tiene respuesta correcta."));
    }

    /** @return Identificador, o {@code null} si no se ha guardado. */
    public Long getId() {
        return id;
    }

    /** @return Enunciado. */
    public String getStatement() {
        return statement;
    }

    /** @return Justificación de la respuesta correcta. */
    public String getJustification() {
        return justification;
    }

    /** @return Opciones (lista inmutable). */
    public List<AnswerOption> getOptions() {
        return options;
    }

    /** @return Estado actual. */
    public QuestionStatus getStatus() {
        return status;
    }

    /** @return Fecha de creación. */
    public Instant getCreatedAt() {
        return createdAt;
    }
}
