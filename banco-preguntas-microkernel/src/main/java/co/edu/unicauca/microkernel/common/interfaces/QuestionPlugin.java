package co.edu.unicauca.microkernel.common.interfaces;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

/**
 * Contrato que deben cumplir estrictamente todos los plugins de
 * generación de preguntas. El núcleo (QuestionMicrokernel) solo conoce
 * esta abstracción, nunca las implementaciones concretas (DIP).
 */
public interface QuestionPlugin {

    /**
     * Nombre identificador del plugin (para trazabilidad/depuración).
     */
    String getName();

    /**
     * Indica si este plugin sabe generar preguntas del tipo indicado.
     */
    boolean supports(String type);

    /**
     * Genera una Question a partir de un QuestionRequest.
     * Debe devolver null si la solicitud no pasa las validaciones
     * propias del plugin (por ejemplo, el pipeline de filtros).
     */
    Question generate(QuestionRequest request);
}
