package co.edu.unicauca.microkernel.pipeline.base;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

/**
 * Contrato de un filtro de validación dentro del pipeline. Cada filtro
 * es responsable de UNA sola regla de validación (SRP), y no conoce a
 * los demás filtros ni al pipeline que los orquesta.
 */
public interface QuestionFilter {

    /**
     * Procesa la solicitud y determina si cumple la regla de este filtro.
     *
     * @param request solicitud a validar
     * @return true si la solicitud pasa esta validación
     */
    boolean process(QuestionRequest request);
}
