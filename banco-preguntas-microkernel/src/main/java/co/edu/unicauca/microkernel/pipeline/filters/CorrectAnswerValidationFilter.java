/**
 * @file CorrectAnswerValidationFilter.java
 * @brief Filtro de respuesta correcta.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/**
 * @brief Valida que la respuesta correcta esté entre las opciones.
 */
public class CorrectAnswerValidationFilter implements QuestionFilter {

    /**
     * @brief Busca la respuesta correcta entre las opciones, sin distinguir mayúsculas.
     * @param request Solicitud a validar
     * @return true si la respuesta existe y está entre las opciones
     */
    @Override
    public boolean process(QuestionRequest request) {
        if (request.getCorrectAnswer() == null || request.getCorrectAnswer().trim().isEmpty()) {
            return false;
        }
        if (request.getOptions() == null) {
            return false;
        }
        return request.getOptions().stream()
                .anyMatch(option -> option != null
                        && option.trim().equalsIgnoreCase(request.getCorrectAnswer().trim()));
    }
}
