/**
 * @file MessagingTopology.java
 * @brief Nombres de exchange, colas y routing keys de RabbitMQ.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.config;

/**
 * @brief Deben coincidir con los de Question Service.
 */
public final class MessagingTopology {

    /** Exchange directa del enunciado. */
    public static final String EXCHANGE = "saberpro.direct.exchange";

    /** Routing key del comando SubmitQuestionForReview. */
    public static final String SUBMIT_ROUTING_KEY = "question.submit";

    /** Cola que consume este servicio. */
    public static final String SUBMIT_QUEUE = "question.submit.review.queue";

    /** Routing key del evento de cambio de estado. */
    public static final String STATUS_CHANGED_ROUTING_KEY = "question.status.changed";

    /** Cola que consume Question Service. */
    public static final String STATUS_CHANGED_QUEUE = "question.status.changed.queue";

    private MessagingTopology() {
    }
}
