/**
 * @file QuestionResponse.java
 * @brief Representación JSON de una pregunta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.rest.dto;

import co.edu.unicauca.saberpro.question.domain.model.Question;

import java.time.Instant;
import java.util.List;

/**
 * @brief Pregunta tal como la ve el cliente HTTP.
 *
 * @param id            Identificador.
 * @param statement     Enunciado.
 * @param justification Justificación.
 * @param options       Opciones.
 * @param correctAnswer Letra de la opción correcta.
 * @param status        Estado actual.
 * @param createdAt     Fecha de creación.
 */
public record QuestionResponse(Long id, String statement, String justification, List<OptionResponse> options,
                               String correctAnswer, String status, Instant createdAt) {

    /**
     * @brief Opción en la respuesta.
     * @param label Letra.
     * @param text  Texto.
     */
    public record OptionResponse(String label, String text) {
    }

    /**
     * @brief Convierte el dominio a DTO.
     * @param q Pregunta de dominio.
     * @return DTO serializable.
     */
    public static QuestionResponse from(Question q) {
        return new QuestionResponse(
                q.getId(), q.getStatement(), q.getJustification(),
                q.getOptions().stream().map(o -> new OptionResponse(o.getLabel(), o.getText())).toList(),
                q.getCorrectOption().getLabel(), q.getStatus().name(), q.getCreatedAt());
    }
}
