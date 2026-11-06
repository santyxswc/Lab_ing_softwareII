/**
 * @file ErrorResponse.java
 * @brief Formato uniforme de los errores HTTP.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.rest.dto;

import java.time.Instant;
import java.util.List;

/**
 * @brief Cuerpo de error.
 *
 * @param timestamp Momento del error.
 * @param status    Código HTTP.
 * @param error     Nombre corto del error.
 * @param message   Descripción.
 * @param details   Lista de detalles (por ejemplo, reglas incumplidas).
 */
public record ErrorResponse(Instant timestamp, int status, String error, String message, List<String> details) {
}
