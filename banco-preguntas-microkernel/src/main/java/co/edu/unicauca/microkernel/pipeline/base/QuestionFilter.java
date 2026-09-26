/**
 * @file QuestionFilter.java
 * @brief Contrato de los filtros de validación.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.pipeline.base;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

/**
 * @brief Filtro del pipeline: valida una sola regla (principio de responsabilidad única).
 *
 * Un filtro no conoce a los demás filtros ni al pipeline que los ejecuta.
 */
public interface QuestionFilter {

    /**
     * @brief Evalúa la regla del filtro.
     * @param request Solicitud a validar
     * @return true si la solicitud cumple la regla
     */
    boolean process(QuestionRequest request);
}
