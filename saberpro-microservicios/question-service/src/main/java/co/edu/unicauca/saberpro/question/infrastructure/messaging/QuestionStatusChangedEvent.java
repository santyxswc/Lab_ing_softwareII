/**
 * @file QuestionStatusChangedEvent.java
 * @brief Evento que Lifecycle Service publica tras cambiar el estado.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.messaging;

import java.time.Instant;

/**
 * @brief Notificación de cambio de estado.
 *
 * @param operationId Operación que originó el cambio.
 * @param questionId  Pregunta afectada.
 * @param newStatus   Nombre del nuevo estado (por ejemplo, "PENDING_REVIEW").
 * @param changedAt   Momento del cambio.
 */
public record QuestionStatusChangedEvent(String operationId, Long questionId, String newStatus, Instant changedAt) {
}
