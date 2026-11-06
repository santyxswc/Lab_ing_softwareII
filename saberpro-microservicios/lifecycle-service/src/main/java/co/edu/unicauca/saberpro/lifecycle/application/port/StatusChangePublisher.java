/**
 * @file StatusChangePublisher.java
 * @brief Puerto de salida para notificar cambios de estado.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.application.port;

import co.edu.unicauca.saberpro.lifecycle.application.dto.QuestionStatusChanged;

/**
 * @brief Publica eventos de cambio de estado en el broker.
 */
public interface StatusChangePublisher {

    /**
     * @brief Publica el evento.
     * @param event Evento.
     */
    void publish(QuestionStatusChanged event);
}
