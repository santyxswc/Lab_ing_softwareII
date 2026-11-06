/**
 * @file RabbitStatusChangePublisher.java
 * @brief Publica QuestionStatusChanged en RabbitMQ.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.messaging;

import co.edu.unicauca.saberpro.lifecycle.application.dto.QuestionStatusChanged;
import co.edu.unicauca.saberpro.lifecycle.application.port.StatusChangePublisher;
import co.edu.unicauca.saberpro.lifecycle.infrastructure.config.MessagingTopology;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @brief Envía a {@code saberpro.direct.exchange} con routing key
 *        {@code question.status.changed}.
 */
@Component
public class RabbitStatusChangePublisher implements StatusChangePublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * @brief Inyecta RabbitTemplate.
     * @param rabbitTemplate Plantilla de envío.
     */
    public RabbitStatusChangePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(QuestionStatusChanged event) {
        rabbitTemplate.convertAndSend(MessagingTopology.EXCHANGE, MessagingTopology.STATUS_CHANGED_ROUTING_KEY, event);
    }
}
