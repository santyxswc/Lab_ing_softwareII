/**
 * @file ReglaNegocioException.java
 * @brief Excepción de dominio lanzada al violar invariantes o reglas de negocio.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.exception;

/**
 * @brief Violación de una regla de negocio del Banco de Preguntas (DDD puro).
 *
 * La usan los Objetos de Valor y el Agregado para reportar invariantes rotas
 * (por ejemplo, una justificación demasiado corta). Al extender
 * {@link DomainException} la capa REST la traduce a HTTP 400.
 */
public class ReglaNegocioException extends DomainException {

    /**
     * @brief Construye la excepción con el mensaje de la regla incumplida.
     * @param message Descripción del error de negocio.
     */
    public ReglaNegocioException(String message) {
        super(message);
    }
}
