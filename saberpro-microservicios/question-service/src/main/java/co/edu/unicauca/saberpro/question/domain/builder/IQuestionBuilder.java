/**
 * @file IQuestionBuilder.java
 * @brief Interfaz del patrón Builder para construir reactivos.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.builder;

import co.edu.unicauca.saberpro.question.domain.model.Question;

/**
 * @brief Construcción fluida de una pregunta de selección múltiple (Punto 1.1).
 *
 * Ejemplo:
 * <pre>
 * Question q = new QuestionBuilder()
 *         .withStatement("¿Cuánto es 2 + 2?")
 *         .withJustification("La suma de dos más dos es cuatro.")
 *         .withOption("A", "3")
 *         .withOption("B", "4")
 *         .withOption("C", "5")
 *         .withOption("D", "22")
 *         .withCorrectAnswer("B")
 *         .build();
 * </pre>
 */
public interface IQuestionBuilder {

    /** Número exacto de opciones que exige un reactivo. */
    int REQUIRED_OPTIONS = 4;

    /**
     * @brief Define el enunciado.
     * @param statement Texto de la pregunta.
     * @return El mismo builder, para encadenar.
     */
    IQuestionBuilder withStatement(String statement);

    /**
     * @brief Define la justificación de la respuesta correcta.
     * @param justification Texto de la justificación.
     * @return El mismo builder.
     */
    IQuestionBuilder withJustification(String justification);

    /**
     * @brief Agrega una opción de respuesta.
     * @param label Letra de la opción (se normaliza a mayúscula).
     * @param text  Texto de la opción.
     * @return El mismo builder.
     */
    IQuestionBuilder withOption(String label, String text);

    /**
     * @brief Indica cuál opción es la correcta.
     * @param label Letra de la opción correcta.
     * @return El mismo builder.
     */
    IQuestionBuilder withCorrectAnswer(String label);

    /**
     * @brief Valida la estructura y crea la pregunta en estado DRAFT.
     * @return Pregunta de dominio lista para validar y guardar.
     * @throws co.edu.unicauca.saberpro.question.domain.exception.InvalidQuestionException
     *         si alguna regla estructural no se cumple.
     */
    Question build();
}
