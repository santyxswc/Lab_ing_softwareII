/**
 * @file ErrorResponse.java
 * @brief Estructura estándar para respuestas de error de la API REST.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.inbound.rest.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String mensaje,
    String ruta
) {}

