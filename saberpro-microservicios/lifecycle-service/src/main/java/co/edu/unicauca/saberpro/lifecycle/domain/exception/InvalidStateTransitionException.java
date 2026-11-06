/**
 * @file InvalidStateTransitionException.java
 * @brief La acción no está permitida en el estado actual.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.exception;

import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionStatus;

/**
 * @brief Lanzada por un estado concreto cuando rechaza una acción.
 */
public class InvalidStateTransitionException extends DomainException {

    /**
     * @brief Construye el mensaje.
     * @param questionId Pregunta afectada.
     * @param current    Estado actual.
     * @param action     Acción intentada.
     */
    public InvalidStateTransitionException(Long questionId, QuestionStatus current, String action) {
        super("La pregunta " + questionId + " está en " + current + " y no se puede " + action + ".");
    }
}
