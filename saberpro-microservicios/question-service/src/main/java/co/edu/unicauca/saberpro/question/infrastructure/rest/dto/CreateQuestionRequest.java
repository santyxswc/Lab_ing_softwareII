/**
 * @file CreateQuestionRequest.java
 * @brief Cuerpo JSON de POST /questions.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.rest.dto;

import co.edu.unicauca.saberpro.question.application.dto.CreateQuestionCommand;

import java.util.List;

/**
 * @brief Petición para crear un reactivo.
 *
 * Las reglas de estructura las valida el Builder, no este DTO, para que
 * haya una sola fuente de verdad.
 *
 * @param statement     Enunciado.
 * @param justification Justificación.
 * @param options       Opciones {label, text}.
 * @param correctAnswer Letra de la opción correcta.
 */
public record CreateQuestionRequest(String statement, String justification,
                                    List<OptionRequest> options, String correctAnswer) {

    /**
     * @brief Opción en la petición.
     * @param label Letra.
     * @param text  Texto.
     */
    public record OptionRequest(String label, String text) {
    }

    /**
     * @brief Convierte el DTO HTTP en comando de aplicación.
     * @return Comando para {@code QuestionService.create}.
     */
    public CreateQuestionCommand toCommand() {
        List<CreateQuestionCommand.OptionData> data = options == null ? null
                : options.stream().map(o -> new CreateQuestionCommand.OptionData(o.label(), o.text())).toList();
        return new CreateQuestionCommand(statement, justification, data, correctAnswer);
    }
}
