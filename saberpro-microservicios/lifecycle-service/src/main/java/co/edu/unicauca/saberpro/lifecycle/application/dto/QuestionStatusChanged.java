/**
 * @file QuestionStatusChanged.java
 * @brief Evento publicado tras un cambio de estado.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.application.dto;

import java.time.Instant;

/**
 * @brief Se publica con routing key {@code question.status.changed} para que
 *        Question Service actualice su copia del estado.
 *
 * @param operationId Operación que originó el cambio.
 * @param questionId  Pregunta afectada.
 * @param newStatus   Nombre del nuevo estado.
 * @param changedAt   Momento del cambio.
 */
public record QuestionStatusChanged(String operationId, Long questionId, String newStatus, Instant changedAt) {
}
