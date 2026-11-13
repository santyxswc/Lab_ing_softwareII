/**
 * @file OpcionResponse.java
 * @brief DTO de respuesta para una opción de respuesta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.inbound.rest.dto;

import co.edu.unicauca.saberpro.domain.model.OpcionRespuesta;

public record OpcionResponse(
    String texto,
    boolean esCorrecta
) {
    public static OpcionResponse fromDomain(OpcionRespuesta domain) {
        return new OpcionResponse(domain.texto(), domain.esCorrecta());
    }
}

