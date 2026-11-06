/**
 * @file PendingReviewState.java
 * @brief Estado concreto "Pendiente de Revisión".
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.state;

import co.edu.unicauca.saberpro.lifecycle.domain.exception.InvalidStateTransitionException;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionStatus;

/**
 * @brief Ya está en revisión: volver a enviarla es una transición inválida.
 */
public class PendingReviewState implements IQuestionState {

    @Override
    public QuestionStatus getStatus() {
        return QuestionStatus.PENDING_REVIEW;
    }

    @Override
    public void submitForReview(QuestionLifecycle context) {
        throw new InvalidStateTransitionException(context.getQuestionId(), getStatus(), "enviar a revisión");
    }
}
