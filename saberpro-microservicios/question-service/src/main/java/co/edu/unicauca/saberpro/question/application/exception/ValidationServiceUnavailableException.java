/**
 * @file ValidationServiceUnavailableException.java
 * @brief Validation Service no respondió o respondió con error.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application.exception;

/**
 * @brief Fallo técnico (no de negocio); se traduce a HTTP 503.
 */
public class ValidationServiceUnavailableException extends RuntimeException {

    /**
     * @brief Construye la excepción.
     * @param message Descripción.
     * @param cause   Error original.
     */
    public ValidationServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
