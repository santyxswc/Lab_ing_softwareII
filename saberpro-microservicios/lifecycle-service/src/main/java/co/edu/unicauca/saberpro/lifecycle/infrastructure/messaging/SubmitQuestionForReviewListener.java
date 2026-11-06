/**
 * @file SubmitQuestionForReviewListener.java
 * @brief Consumidor AMQP de question.submit.review.queue.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.messaging;

import co.edu.unicauca.saberpro.lifecycle.application.LifecycleService;
import co.edu.unicauca.saberpro.lifecycle.application.dto.SubmitQuestionForReview;
import co.edu.unicauca.saberpro.lifecycle.infrastructure.config.MessagingTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @brief Recibe SubmitQuestionForReview y ejecuta la transición (Punto 2.2).
 *
 * El método es transaccional: el ciclo de vida y el registro de operación
 * se guardan juntos o no se guardan.
 */
@Component
public class SubmitQuestionForReviewListener {

    private static final Logger log = LoggerFactory.getLogger(SubmitQuestionForReviewListener.class);

    private final LifecycleService service;

    /**
     * @brief Inyecta el caso de uso.
     * @param service Servicio de ciclo de vida.
     */
    public SubmitQuestionForReviewListener(LifecycleService service) {
        this.service = service;
    }

    /**
     * @brief Procesa un comando.
     * @param command Mensaje deserializado desde JSON.
     */
    @RabbitListener(queues = MessagingTopology.SUBMIT_QUEUE)
    @Transactional
    public void onSubmit(SubmitQuestionForReview command) {
        log.info("Recibido SubmitQuestionForReview operationId={} questionId={}",
                command.operationId(), command.questionId());
        service.handle(command).ifPresentOrElse(
                op -> log.info("Operación {} → {}: {}", op.operationId(), op.outcome(), op.detail()),
                () -> log.info("Operación {} ya procesada; se ignora el duplicado.", command.operationId()));
    }
}
