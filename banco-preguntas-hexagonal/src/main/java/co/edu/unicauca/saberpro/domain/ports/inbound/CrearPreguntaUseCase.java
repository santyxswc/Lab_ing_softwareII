/**
 * @file CrearPreguntaUseCase.java
 * @brief Puerto de Entrada (Driving / Inbound) para la creación y envío a revisión de preguntas.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.ports.inbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;

/**
 * Puerto de Entrada (Driving) de la Arquitectura Hexagonal.
 * Define la operación de creación y envío a revisión de un reactivo.
 */
public interface CrearPreguntaUseCase {

    /**
     * Orquesta la creación del reactivo en BORRADOR, la validación de sus invariantes
     * y su posterior envío a revisión (PENDIENTE_REVISION), persistiendo el resultado.
     *
     * @param command Comando con la información del reactivo y sus opciones.
     * @return El Agregado {@link Pregunta} en estado PENDIENTE_REVISION y persistido.
     */
    Pregunta crearPregunta(CrearPreguntaCommand command);
}

