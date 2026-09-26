/**
 * @file OptionsValidationFilter.java
 * @brief Filtro de opciones de respuesta.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/**
 * @brief Valida que haya exactamente 4 opciones y que ninguna esté vacía.
 */
public class OptionsValidationFilter implements QuestionFilter {

    /** Número de opciones requerido. */
    private static final int REQUIRED_OPTIONS = 4;

    /**
     * @brief Valida la cantidad y el contenido de las opciones.
     * @param request Solicitud a validar
     * @return true si hay 4 opciones no vacías
     */
    @Override
    public boolean process(QuestionRequest request) {
        if (request.getOptions() == null || request.getOptions().size() != REQUIRED_OPTIONS) {
            return false;
        }
        return request.getOptions().stream()
                .allMatch(option -> option != null && !option.trim().isEmpty());
    }
}
