/**
 * @file QuestionStateTest.java
 * @brief Pruebas del patrón State (Punto 1.2).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.state;

import co.edu.unicauca.saberpro.lifecycle.domain.exception.InvalidStateTransitionException;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @brief Verifica Draft → PendingReview y el rechazo de la transición repetida.
 */
class QuestionStateTest {

    /** @brief Un ciclo nuevo empieza en DraftState. */
    @Test
    void startsInDraft() {
        QuestionLifecycle lc = QuestionLifecycle.startAsDraft(1L);
        assertInstanceOf(DraftState.class, lc.getState());
        assertEquals(QuestionStatus.DRAFT, lc.getStatus());
    }

    /** @brief Draft + submitForReview → PendingReviewState. */
    @Test
    void draftMovesToPendingReview() {
        QuestionLifecycle lc = QuestionLifecycle.startAsDraft(1L);
        lc.submitForReview("op-1");
        assertInstanceOf(PendingReviewState.class, lc.getState());
        assertEquals(QuestionStatus.PENDING_REVIEW, lc.getStatus());
        assertEquals("op-1", lc.getLastOperationId());
    }

    /** @brief PendingReview + submitForReview → excepción y el estado no cambia. */
    @Test
    void pendingReviewRejectsSecondSubmit() {
        QuestionLifecycle lc = QuestionLifecycle.startAsDraft(1L);
        lc.submitForReview("op-1");
        assertThrows(InvalidStateTransitionException.class, () -> lc.submitForReview("op-2"));
        assertEquals(QuestionStatus.PENDING_REVIEW, lc.getStatus());
        assertEquals("op-1", lc.getLastOperationId());
    }

    /** @brief Reconstruir desde el enumerado devuelve el estado concreto correcto. */
    @Test
    void restoreBuildsConcreteState() {
        assertInstanceOf(DraftState.class,
                QuestionLifecycle.restore(1L, QuestionStatus.DRAFT, null, null).getState());
        assertInstanceOf(PendingReviewState.class,
                QuestionLifecycle.restore(1L, QuestionStatus.PENDING_REVIEW, null, null).getState());
    }
}
