/**
 * @file DomainException.java
 * @brief Base de todos los errores del dominio.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.exception;

/**
 * @brief Error de negocio (mostrable al usuario).
 */
public class DomainException extends RuntimeException {

    /**
     * @brief Construye la excepción.
     * @param message Descripción del error.
     */
    public DomainException(String message) {
        super(message);
    }
}
