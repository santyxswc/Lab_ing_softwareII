/**
 * @file InvalidQuestionException.java
 * @brief La estructura del reactivo no cumple las reglas.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.exception;

import java.util.List;

/**
 * @brief Lanzada por {@code QuestionBuilder.build()} con todos los errores
 *        encontrados, no solo el primero.
 */
public class InvalidQuestionException extends DomainException {

    private final List<String> errors;

    /**
     * @brief Construye la excepción con la lista de problemas.
     * @param errors Mensajes de cada regla incumplida.
     */
    public InvalidQuestionException(List<String> errors) {
        super("La pregunta no es válida: " + String.join(" ", errors));
        this.errors = List.copyOf(errors);
    }

    /** @return Lista de reglas incumplidas. */
    public List<String> getErrors() {
        return errors;
    }
}
