/**
 * @file RestExceptionHandler.java
 * @brief Traduce las excepciones a respuestas HTTP.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.rest;

import co.edu.unicauca.saberpro.question.application.exception.ValidationRejectedException;
import co.edu.unicauca.saberpro.question.application.exception.ValidationServiceUnavailableException;
import co.edu.unicauca.saberpro.question.domain.exception.InvalidQuestionException;
import co.edu.unicauca.saberpro.question.domain.exception.QuestionNotFoundException;
import co.edu.unicauca.saberpro.question.infrastructure.rest.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

/**
 * @brief Tabla de traducción:
 * <ul>
 *   <li>{@link InvalidQuestionException} → 400 (estructura inválida).</li>
 *   <li>{@link ValidationRejectedException} → 400 (justificación rechazada).</li>
 *   <li>{@link QuestionNotFoundException} → 404.</li>
 *   <li>{@link ValidationServiceUnavailableException} → 503.</li>
 *   <li>{@link AmqpException} (RabbitMQ caído) → 503.</li>
 * </ul>
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * @brief Estructura del reactivo inválida.
     * @param e Excepción del Builder.
     * @return 400 con la lista de errores.
     */
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> handleInvalid(InvalidQuestionException e) {
        return build(HttpStatus.BAD_REQUEST, "La estructura de la pregunta no es válida.", e.getErrors());
    }

    /**
     * @brief Justificación rechazada por Validation Service.
     * @param e Excepción.
     * @return 400; la pregunta no se guardó.
     */
    @ExceptionHandler(ValidationRejectedException.class)
    public ResponseEntity<ErrorResponse> handleRejected(ValidationRejectedException e) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), List.of());
    }

    /**
     * @brief Pregunta inexistente.
     * @param e Excepción.
     * @return 404.
     */
    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(QuestionNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage(), List.of());
    }

    /**
     * @brief Validation Service caído.
     * @param e Excepción.
     * @return 503.
     */
    @ExceptionHandler(ValidationServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleValidationDown(ValidationServiceUnavailableException e) {
        log.error("Validation Service no disponible", e);
        return build(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), List.of());
    }

    /**
     * @brief RabbitMQ caído al publicar.
     * @param e Excepción de Spring AMQP.
     * @return 503.
     */
    @ExceptionHandler(AmqpException.class)
    public ResponseEntity<ErrorResponse> handleBrokerDown(AmqpException e) {
        log.error("No se pudo publicar en RabbitMQ", e);
        return build(HttpStatus.SERVICE_UNAVAILABLE, "El broker de mensajería no está disponible.", List.of());
    }

    private static ResponseEntity<ErrorResponse> build(HttpStatus status, String message, List<String> details) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, details));
    }
}
