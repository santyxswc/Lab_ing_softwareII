/**
 * @file DomainException.java
 * @brief Base de todos los errores del dominio.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.exception;

/**
 * @brief Error de negocio (mostrable al usuario).
 *
 * Las capas externas lo atrapan para distinguir un error del negocio de un
 * fallo técnico.
 */
public class DomainException extends RuntimeException {

    /**
     * @brief Construye la excepción con un mensaje.
     * @param message Descripción del error.
     */
    public DomainException(String message) {
        super(message);
    }
}
