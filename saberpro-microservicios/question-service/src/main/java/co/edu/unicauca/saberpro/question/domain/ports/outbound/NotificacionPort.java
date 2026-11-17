/**
 * @file NotificacionPort.java
 * @brief Puerto de Salida (Driven / Outbound) para notificar eventos a otros servicios.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.ports.outbound;

import java.time.Instant;

/**
 * @brief Puerto de Salida (Driven) para notificar a otros microservicios.
 *
 * Su implementación ({@code RabbitMQPublisher}) usa RabbitMQ. El puerto es
 * Java puro: recibe tipos del dominio y no conoce AMQP.
 */
public interface NotificacionPort {

    /**
     * @brief Notifica la solicitud de evaluación (revisión) de un reactivo.
     *
     * @param operationId  Id único de la operación asíncrona.
     * @param questionId   Reactivo a enviar.
     * @param solicitadoEn Momento de la solicitud.
     */
    void notificarRevision(String operationId, Long questionId, Instant solicitadoEn);
}
