/**
 * @file QuestionStatus.java
 * @brief Estados del ciclo de vida y su estado concreto (patrón State).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.model;

import co.edu.unicauca.saberpro.lifecycle.domain.state.DraftState;
import co.edu.unicauca.saberpro.lifecycle.domain.state.IQuestionState;
import co.edu.unicauca.saberpro.lifecycle.domain.state.PendingReviewState;

import java.util.function.Supplier;

/**
 * @brief Nombre persistible de cada estado.
 *
 * Cada constante sabe fabricar su objeto estado, así reconstruir el estado
 * desde la base de datos tampoco necesita {@code if} ni {@code switch}.
 */
public enum QuestionStatus {
    /** Borrador. */
    DRAFT(DraftState::new),
    /** Pendiente de revisión. */
    PENDING_REVIEW(PendingReviewState::new);

    private final Supplier<IQuestionState> factory;

    QuestionStatus(Supplier<IQuestionState> factory) {
        this.factory = factory;
    }

    /**
     * @brief Crea el objeto estado concreto asociado.
     * @return Instancia nueva del estado.
     */
    public IQuestionState toState() {
        return factory.get();
    }
}
