/**
 * @file MessagingTopology.java
 * @brief Nombres de exchange, colas y routing keys de RabbitMQ.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.config;

/**
 * @brief Constantes compartidas por la configuración, el publicador y el listener.
 *
 * Deben coincidir con las de Lifecycle Service.
 */
public final class MessagingTopology {

    /** Exchange directa del enunciado. */
    public static final String EXCHANGE = "saberpro.direct.exchange";

    /** Routing key del comando SubmitQuestionForReview. */
    public static final String SUBMIT_ROUTING_KEY = "question.submit";

    /** Cola que consume Lifecycle Service. */
    public static final String SUBMIT_QUEUE = "question.submit.review.queue";

    /** Routing key del evento de cambio de estado (Lifecycle → Question). */
    public static final String STATUS_CHANGED_ROUTING_KEY = "question.status.changed";

    /** Cola que consume Question Service para replicar el estado. */
    public static final String STATUS_CHANGED_QUEUE = "question.status.changed.queue";

    private MessagingTopology() {
    }
}
