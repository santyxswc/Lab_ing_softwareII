/**
 * @file CrearPreguntaRequest.java
 * @brief DTO de entrada para la petición HTTP POST /api/preguntas.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.inbound.rest.dto;

import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CrearPreguntaRequest(
    @NotBlank(message = "El enunciado es obligatorio.")
    String enunciado,

    @NotBlank(message = "La justificación es obligatoria.")
    @Size(min = 15, message = "La justificación debe tener al menos 15 caracteres de sustentación técnica.")
    String justificacion,

    @NotNull(message = "La lista de opciones es obligatoria.")
    @Size(min = 4, max = 4, message = "Debe proveer exactamente 4 opciones de respuesta.")
    @Valid
    List<OpcionRequest> opciones
) {
    public record OpcionRequest(
        @NotBlank(message = "El texto de la opción es obligatorio.")
        String texto,

        boolean esCorrecta
    ) {}

    public CrearPreguntaCommand toCommand() {
        List<CrearPreguntaCommand.OpcionItem> items = opciones == null ? List.of() :
            opciones.stream()
                .map(o -> new CrearPreguntaCommand.OpcionItem(o.texto(), o.esCorrecta()))
                .toList();
        return new CrearPreguntaCommand(enunciado, justificacion, items);
    }
}

