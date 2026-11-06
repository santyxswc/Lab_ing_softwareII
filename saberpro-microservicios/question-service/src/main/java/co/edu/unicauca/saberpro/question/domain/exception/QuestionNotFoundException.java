/**
 * @file QuestionNotFoundException.java
 * @brief Se buscó una pregunta que no existe.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.exception;

/**
 * @brief Error de entidad no encontrada.
 */
public class QuestionNotFoundException extends DomainException {

    /**
     * @brief Construye el mensaje a partir del id.
     * @param id Identificador que no se encontró.
     */
    public QuestionNotFoundException(Long id) {
        super("Pregunta con ID " + id + " no existe.");
    }
}
