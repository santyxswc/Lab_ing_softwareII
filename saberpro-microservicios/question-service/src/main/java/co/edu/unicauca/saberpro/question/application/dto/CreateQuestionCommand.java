/**
 * @file CreateQuestionCommand.java
 * @brief Datos de entrada del caso de uso "crear pregunta".
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application.dto;

import java.util.List;

/**
 * @brief Comando para crear un reactivo.
 *
 * @param statement     Enunciado.
 * @param justification Justificación de la respuesta correcta.
 * @param options       Opciones de respuesta.
 * @param correctAnswer Letra de la opción correcta.
 */
public record CreateQuestionCommand(String statement, String justification,
                                    List<OptionData> options, String correctAnswer) {

    /**
     * @brief Una opción tal como llega del cliente.
     *
     * @param label Letra.
     * @param text  Texto.
     */
    public record OptionData(String label, String text) {
    }
}
