/**
 * @file SubmitQuestionForReview.java
 * @brief Comando AMQP que pide enviar una pregunta a revisión.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application.dto;

import java.time.Instant;

/**
 * @brief Mensaje publicado en {@code saberpro.direct.exchange} con routing key
 *        {@code question.submit} (Punto 2.2).
 *
 * Lifecycle Service lo consume y ejecuta la transición a PENDING_REVIEW.
 *
 * @param operationId Id único de la operación asíncrona (se devuelve en el 202).
 * @param questionId  Pregunta a enviar.
 * @param requestedAt Momento de la solicitud.
 */
public record SubmitQuestionForReview(String operationId, Long questionId, Instant requestedAt) {
}
