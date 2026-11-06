/**
 * @file DraftState.java
 * @brief Estado concreto "Borrador".
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.state;

import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionStatus;

/**
 * @brief En borrador, enviar a revisión es válido: pasa a {@link PendingReviewState}.
 */
public class DraftState implements IQuestionState {

    @Override
    public QuestionStatus getStatus() {
        return QuestionStatus.DRAFT;
    }

    @Override
    public void submitForReview(QuestionLifecycle context) {
        context.changeState(new PendingReviewState());
    }
}
