package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/**
 * Valida que la respuesta correcta declarada esté efectivamente
 * presente dentro de la lista de opciones de la solicitud.
 */
public class CorrectAnswerValidationFilter implements QuestionFilter {

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
