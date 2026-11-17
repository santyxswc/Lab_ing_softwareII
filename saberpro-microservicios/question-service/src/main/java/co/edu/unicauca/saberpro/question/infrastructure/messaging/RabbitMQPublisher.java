/**
 * @file RabbitMQPublisher.java
 * @brief Adaptador de salida que publica notificaciones en RabbitMQ.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.messaging;

import co.edu.unicauca.saberpro.question.application.dto.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.NotificacionPort;
import co.edu.unicauca.saberpro.question.infrastructure.config.MessagingTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @brief Implementa {@link NotificacionPort} con RabbitTemplate (Punto 2.2).
 *
 * Envía a {@code saberpro.direct.exchange} con routing key {@code question.submit}.
 */
@Component
public class RabbitMQPublisher implements NotificacionPort {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    /**
     * @brief Inyecta RabbitTemplate (usa el conversor JSON de {@code RabbitConfig}).
     * @param rabbitTemplate Plantilla de envío.
     */
    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void notificarRevision(String operationId, Long questionId, Instant solicitadoEn) {
        SubmitQuestionForReview command = new SubmitQuestionForReview(operationId, questionId, solicitadoEn);
        rabbitTemplate.convertAndSend(MessagingTopology.EXCHANGE, MessagingTopology.SUBMIT_ROUTING_KEY, command);
        log.info("Publicado SubmitQuestionForReview operationId={} questionId={}", operationId, questionId);
    }
}
