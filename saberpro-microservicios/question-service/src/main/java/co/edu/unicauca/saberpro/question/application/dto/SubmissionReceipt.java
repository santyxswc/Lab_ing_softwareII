/**
 * @file SubmissionReceipt.java
 * @brief Comprobante de que el envío a revisión fue aceptado.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application.dto;

/**
 * @brief Resultado inmediato del envío asíncrono.
 *
 * @param operationId Id para rastrear la operación.
 * @param questionId  Pregunta enviada.
 */
public record SubmissionReceipt(String operationId, Long questionId) {
}
