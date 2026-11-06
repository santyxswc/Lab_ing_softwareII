/**
 * @file ProcessedOperation.java
 * @brief Registro de una operación asíncrona ya procesada.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.model;

import java.time.Instant;

/**
 * @brief Permite consultar qué pasó con el operationId devuelto en el 202
 *        y evita procesar dos veces el mismo mensaje.
 *
 * @param operationId Id de la operación.
 * @param questionId  Pregunta afectada.
 * @param outcome     Resultado.
 * @param detail      Explicación (por ejemplo, "DRAFT -> PENDING_REVIEW").
 * @param processedAt Momento del procesamiento.
 */
public record ProcessedOperation(String operationId, Long questionId, OperationOutcome outcome,
                                 String detail, Instant processedAt) {
}
