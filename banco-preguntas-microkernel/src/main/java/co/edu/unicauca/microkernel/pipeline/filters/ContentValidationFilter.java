/**
 * @file ContentValidationFilter.java
 * @brief Filtro de título y contenido.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/**
 * @brief Valida que el título y el contenido no estén vacíos.
 */
public class ContentValidationFilter implements QuestionFilter {

    /**
     * @brief Valida título y contenido.
     * @param request Solicitud a validar
     * @return true si ninguno está vacío
     */
    @Override
    public boolean process(QuestionRequest request) {
        return request.getTitle() != null && !request.getTitle().trim().isEmpty()
                && request.getContent() != null && !request.getContent().trim().isEmpty();
    }
}
