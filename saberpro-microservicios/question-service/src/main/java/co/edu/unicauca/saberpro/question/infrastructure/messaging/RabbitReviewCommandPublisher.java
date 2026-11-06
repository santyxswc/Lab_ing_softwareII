/**
 * @file RabbitReviewCommandPublisher.java
 * @brief Publica SubmitQuestionForReview en RabbitMQ.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.messaging;

import co.edu.unicauca.saberpro.question.application.dto.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.question.application.port.ReviewCommandPublisher;
import co.edu.unicauca.saberpro.question.infrastructure.config.MessagingTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @brief Implementa {@link ReviewCommandPublisher} con RabbitTemplate (Punto 2.2).
 *
 * Envía a {@code saberpro.direct.exchange} con routing key {@code question.submit}.
 */
@Component
public class RabbitReviewCommandPublisher implements ReviewCommandPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitReviewCommandPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    /**
     * @brief Inyecta RabbitTemplate (usa el conversor JSON de {@code RabbitConfig}).
     * @param rabbitTemplate Plantilla de envío.
     */
    public RabbitReviewCommandPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(SubmitQuestionForReview command) {
        rabbitTemplate.convertAndSend(MessagingTopology.EXCHANGE, MessagingTopology.SUBMIT_ROUTING_KEY, command);
        log.info("Publicado SubmitQuestionForReview operationId={} questionId={}",
                command.operationId(), command.questionId());
    }
}
