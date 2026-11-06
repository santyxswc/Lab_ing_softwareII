/**
 * @file ValidationRejectedException.java
 * @brief Validation Service rechazó la justificación.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application.exception;

import co.edu.unicauca.saberpro.question.domain.exception.DomainException;

/**
 * @brief Se traduce a HTTP 400; la pregunta NO se guarda.
 */
public class ValidationRejectedException extends DomainException {

    /**
     * @brief Construye la excepción con el motivo del rechazo.
     * @param message Mensaje devuelto por Validation Service.
     */
    public ValidationRejectedException(String message) {
        super(message);
    }
}
