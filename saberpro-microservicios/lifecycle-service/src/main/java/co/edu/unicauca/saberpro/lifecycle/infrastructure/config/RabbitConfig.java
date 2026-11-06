/**
 * @file RabbitConfig.java
 * @brief Declara la topología AMQP y el conversor JSON.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @brief Exchange, colas y bindings que Spring crea en RabbitMQ al arrancar.
 *
 * Las declaraciones son idempotentes: Question Service declara las mismas,
 * así no importa cuál servicio arranque primero.
 */
@Configuration
public class RabbitConfig {

    /** @return Exchange directa {@code saberpro.direct.exchange}. */
    @Bean
    public DirectExchange saberProExchange() {
        return new DirectExchange(MessagingTopology.EXCHANGE, true, false);
    }

    /** @return Cola durable {@code question.submit.review.queue}. */
    @Bean
    public Queue submitReviewQueue() {
        return QueueBuilder.durable(MessagingTopology.SUBMIT_QUEUE).build();
    }

    /** @return Cola durable {@code question.status.changed.queue}. */
    @Bean
    public Queue statusChangedQueue() {
        return QueueBuilder.durable(MessagingTopology.STATUS_CHANGED_QUEUE).build();
    }

    /**
     * @brief Enlaza la cola de envío a revisión con la routing key {@code question.submit}.
     * @param submitReviewQueue Cola destino.
     * @param saberProExchange  Exchange origen.
     * @return Binding.
     */
    @Bean
    public Binding submitBinding(Queue submitReviewQueue, DirectExchange saberProExchange) {
        return BindingBuilder.bind(submitReviewQueue).to(saberProExchange).with(MessagingTopology.SUBMIT_ROUTING_KEY);
    }

    /**
     * @brief Enlaza la cola de cambios de estado con su routing key.
     * @param statusChangedQueue Cola destino.
     * @param saberProExchange   Exchange origen.
     * @return Binding.
     */
    @Bean
    public Binding statusChangedBinding(Queue statusChangedQueue, DirectExchange saberProExchange) {
        return BindingBuilder.bind(statusChangedQueue).to(saberProExchange)
                .with(MessagingTopology.STATUS_CHANGED_ROUTING_KEY);
    }

    /**
     * @brief Serializa los mensajes como JSON.
     *
     * {@code alwaysConvertToInferredType} hace que el listener deserialice
     * según el tipo de su parámetro y no según el nombre de clase del emisor
     * (cada microservicio tiene sus propios paquetes).
     *
     * @param objectMapper ObjectMapper de Spring (soporta fechas java.time).
     * @return Conversor que usan RabbitTemplate y los listeners.
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }
}
