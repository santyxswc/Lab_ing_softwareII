/**
 * @file SubmitQuestionForReview.java
 * @brief Comando AMQP recibido desde Question Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.application.dto;

import java.time.Instant;

/**
 * @brief Contrato del mensaje en {@code question.submit.review.queue}.
 *
 * Copia del mismo record en Question Service: cada microservicio es dueño
 * de sus clases y solo comparten el formato JSON.
 *
 * @param operationId Id de la operación.
 * @param questionId  Pregunta a enviar a revisión.
 * @param requestedAt Momento de la solicitud.
 */
public record SubmitQuestionForReview(String operationId, Long questionId, Instant requestedAt) {
}
