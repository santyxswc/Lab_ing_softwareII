/**
 * @file QuestionLifecycle.java
 * @brief Contexto del patrón State: el ciclo de vida de una pregunta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.model;

import co.edu.unicauca.saberpro.lifecycle.domain.state.DraftState;
import co.edu.unicauca.saberpro.lifecycle.domain.state.IQuestionState;

import java.time.Instant;
import java.util.Objects;

/**
 * @brief Guarda el estado actual y le delega las acciones.
 *
 * Java puro, sin anotaciones de persistencia. Ejemplo:
 * <pre>
 * QuestionLifecycle lc = QuestionLifecycle.startAsDraft(1L);
 * lc.submitForReview("op-1");   // DraftState → PendingReviewState
 * lc.submitForReview("op-2");   // lanza InvalidStateTransitionException
 * </pre>
 */
public final class QuestionLifecycle {

    private final Long questionId;
    private IQuestionState state;
    private String lastOperationId;
    private Instant updatedAt;

    private QuestionLifecycle(Long questionId, IQuestionState state, String lastOperationId, Instant updatedAt) {
        this.questionId = Objects.requireNonNull(questionId, "questionId");
        this.state = Objects.requireNonNull(state, "state");
        this.lastOperationId = lastOperationId;
        this.updatedAt = updatedAt;
    }

    /**
     * @brief Ciclo de vida nuevo: toda pregunta empieza en borrador.
     * @param questionId Pregunta asociada.
     * @return Ciclo de vida en {@link DraftState}.
     */
    public static QuestionLifecycle startAsDraft(Long questionId) {
        return new QuestionLifecycle(questionId, new DraftState(), null, Instant.now());
    }

    /**
     * @brief Reconstruye un ciclo de vida guardado (lo usa el mapeador).
     * @param questionId      Pregunta asociada.
     * @param status          Estado guardado.
     * @param lastOperationId Última operación aplicada.
     * @param updatedAt       Fecha del último cambio.
     * @return Ciclo de vida con el objeto estado correspondiente.
     */
    public static QuestionLifecycle restore(Long questionId, QuestionStatus status,
                                            String lastOperationId, Instant updatedAt) {
        return new QuestionLifecycle(questionId, status.toState(), lastOperationId, updatedAt);
    }

    /**
     * @brief Envía la pregunta a revisión delegando en el estado actual.
     * @param operationId Operación que solicita el cambio.
     * @throws co.edu.unicauca.saberpro.lifecycle.domain.exception.InvalidStateTransitionException
     *         si el estado actual no lo permite.
     */
    public void submitForReview(String operationId) {
        state.submitForReview(this);
        this.lastOperationId = operationId;
        this.updatedAt = Instant.now();
    }

    /**
     * @brief Cambia el estado. Solo lo invocan los estados concretos.
     * @param newState Estado siguiente.
     */
    public void changeState(IQuestionState newState) {
        this.state = Objects.requireNonNull(newState, "newState");
    }

    /** @return Pregunta asociada. */
    public Long getQuestionId() {
        return questionId;
    }

    /** @return Estado actual como enumerado. */
    public QuestionStatus getStatus() {
        return state.getStatus();
    }

    /** @return Objeto estado actual. */
    public IQuestionState getState() {
        return state;
    }

    /** @return Última operación aplicada, o {@code null}. */
    public String getLastOperationId() {
        return lastOperationId;
    }

    /** @return Fecha del último cambio. */
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
