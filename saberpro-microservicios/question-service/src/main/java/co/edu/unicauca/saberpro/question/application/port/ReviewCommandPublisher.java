/**
 * @file ReviewCommandPublisher.java
 * @brief Puerto de salida para publicar el comando de envío a revisión.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application.port;

import co.edu.unicauca.saberpro.question.application.dto.SubmitQuestionForReview;

/**
 * @brief Publica comandos en el broker de mensajería.
 *
 * La implementación ({@code RabbitReviewCommandPublisher}) usa RabbitMQ.
 */
public interface ReviewCommandPublisher {

    /**
     * @brief Publica el comando SubmitQuestionForReview.
     * @param command Comando a publicar.
     */
    void publish(SubmitQuestionForReview command);
}
