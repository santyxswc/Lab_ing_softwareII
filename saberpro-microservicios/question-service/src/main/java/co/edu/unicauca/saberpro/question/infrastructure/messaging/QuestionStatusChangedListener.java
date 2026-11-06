/**
 * @file QuestionStatusChangedListener.java
 * @brief Consume los cambios de estado que publica Lifecycle Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.messaging;

import co.edu.unicauca.saberpro.question.application.QuestionService;
import co.edu.unicauca.saberpro.question.domain.exception.DomainException;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.question.infrastructure.config.MessagingTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @brief Replica en la base de Question Service el estado calculado por
 *        Lifecycle Service, para que {@code GET /questions/{id}} lo muestre.
 */
@Component
public class QuestionStatusChangedListener {

    private static final Logger log = LoggerFactory.getLogger(QuestionStatusChangedListener.class);

    private final QuestionService service;

    /**
     * @brief Inyecta el caso de uso.
     * @param service Servicio de preguntas.
     */
    public QuestionStatusChangedListener(QuestionService service) {
        this.service = service;
    }

    /**
     * @brief Procesa un evento de {@code question.status.changed.queue}.
     *
     * Los errores de negocio se registran y el mensaje se descarta (no tiene
     * sentido reintentarlo).
     *
     * @param event Evento recibido.
     */
    @RabbitListener(queues = MessagingTopology.STATUS_CHANGED_QUEUE)
    public void onStatusChanged(QuestionStatusChangedEvent event) {
        try {
            service.syncStatus(event.questionId(), QuestionStatus.valueOf(event.newStatus()));
            log.info("Pregunta {} ahora está en {} (operationId={})",
                    event.questionId(), event.newStatus(), event.operationId());
        } catch (DomainException | IllegalArgumentException e) {
            log.warn("Evento descartado {}: {}", event, e.getMessage());
        }
    }
}
