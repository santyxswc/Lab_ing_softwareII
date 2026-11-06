/**
 * @file SubmitAcceptedResponse.java
 * @brief Cuerpo de la respuesta 202 Accepted de POST /questions/{id}/submit.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.rest.dto;

/**
 * @brief Confirma que el comando se publicó; el cambio de estado ocurre después.
 *
 * @param operationId Id para rastrear la operación (también en Lifecycle Service).
 * @param questionId  Pregunta enviada.
 * @param message     Texto informativo.
 */
public record SubmitAcceptedResponse(String operationId, Long questionId, String message) {
}
