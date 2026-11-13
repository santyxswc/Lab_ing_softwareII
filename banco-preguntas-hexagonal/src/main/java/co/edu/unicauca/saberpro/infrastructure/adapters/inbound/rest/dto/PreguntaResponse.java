/**
 * @file PreguntaResponse.java
 * @brief DTO de respuesta para el reactivo devuelto por la API REST.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.inbound.rest.dto;

import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;

import java.util.List;

public record PreguntaResponse(
    Long id,
    String enunciado,
    String justificacion,
    EstadoPregunta estado,
    List<OpcionResponse> opciones
) {
    public static PreguntaResponse fromDomain(Pregunta domain) {
        if (domain == null) {
            return null;
        }

        List<OpcionResponse> opciones = domain.getOpciones().stream()
            .map(OpcionResponse::fromDomain)
            .toList();

        return new PreguntaResponse(
            domain.getId(),
            domain.getEnunciado().valor(),
            domain.getJustificacion().valor(),
            domain.getEstado(),
            opciones
        );
    }
}

