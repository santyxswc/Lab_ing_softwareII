/**
 * @file QuestionStatus.java
 * @brief Estados posibles del ciclo de vida de una pregunta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.model;

/**
 * @brief Estado de una pregunta tal como lo conoce Question Service.
 *
 * La transición entre estados la ejecuta Lifecycle Service (patrón State);
 * este servicio solo guarda el estado resultante que le notifican.
 */
public enum QuestionStatus {
    /** Borrador: recién creada y validada. */
    DRAFT,
    /** Enviada a revisión por el comité. */
    PENDING_REVIEW
}
