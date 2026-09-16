package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/**
 * Valida que la solicitud tenga exactamente 4 opciones de respuesta,
 * y que ninguna de ellas esté vacía.
 */
public class OptionsValidationFilter implements QuestionFilter {

    private static final int REQUIRED_OPTIONS = 4;

    @Override
    public boolean process(QuestionRequest request) {
        if (request.getOptions() == null || request.getOptions().size() != REQUIRED_OPTIONS) {
            return false;
        }
        return request.getOptions().stream()
                .allMatch(option -> option != null && !option.trim().isEmpty());
    }
}
