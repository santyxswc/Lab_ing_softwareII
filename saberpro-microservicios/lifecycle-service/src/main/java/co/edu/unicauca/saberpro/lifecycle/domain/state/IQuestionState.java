/**
 * @file IQuestionState.java
 * @brief Interfaz del patrón State para el ciclo de vida de una pregunta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.state;

import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionStatus;

/**
 * @brief Comportamiento que cambia según el estado de la pregunta (Punto 1.2).
 *
 * El contexto ({@link QuestionLifecycle}) delega en el estado actual; cada
 * estado concreto decide a qué estado pasar. Por eso no hay
 * {@code if/else} ni {@code switch} sobre el estado en ningún lugar.
 */
public interface IQuestionState {

    /**
     * @brief Nombre del estado (para persistir y mostrar).
     * @return Estado como enumerado.
     */
    QuestionStatus getStatus();

    /**
     * @brief Acción "enviar a revisión".
     * @param context Ciclo de vida sobre el que se actúa.
     * @throws co.edu.unicauca.saberpro.lifecycle.domain.exception.InvalidStateTransitionException
     *         si el estado actual no permite la acción.
     */
    void submitForReview(QuestionLifecycle context);
}
