/**
 * @file LifecycleController.java
 * @brief Endpoints de consulta de Lifecycle Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.rest;

import co.edu.unicauca.saberpro.lifecycle.application.LifecycleService;
import co.edu.unicauca.saberpro.lifecycle.domain.exception.NotFoundException;
import co.edu.unicauca.saberpro.lifecycle.domain.model.ProcessedOperation;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * @brief Permite verificar en Postman el resultado del procesamiento asíncrono.
 *
 * <ul>
 *   <li>{@code GET /lifecycle/questions/{questionId}} → estado actual.</li>
 *   <li>{@code GET /lifecycle/operations/{operationId}} → resultado de la operación.</li>
 * </ul>
 */
@RestController
@RequestMapping("/lifecycle")
public class LifecycleController {

    /**
     * @brief Vista JSON del ciclo de vida.
     * @param questionId      Pregunta.
     * @param status          Estado.
     * @param lastOperationId Última operación.
     * @param updatedAt       Último cambio.
     */
    public record LifecycleResponse(Long questionId, String status, String lastOperationId, Instant updatedAt) {
    }

    private final LifecycleService service;

    /**
     * @brief Inyecta el caso de uso.
     * @param service Servicio de ciclo de vida.
     */
    public LifecycleController(LifecycleService service) {
        this.service = service;
    }

    /**
     * @brief Estado de una pregunta.
     * @param questionId Pregunta.
     * @return Ciclo de vida.
     */
    @GetMapping("/questions/{questionId}")
    public LifecycleResponse getLifecycle(@PathVariable Long questionId) {
        QuestionLifecycle lc = service.getLifecycle(questionId);
        return new LifecycleResponse(lc.getQuestionId(), lc.getStatus().name(),
                lc.getLastOperationId(), lc.getUpdatedAt());
    }

    /**
     * @brief Resultado de una operación asíncrona.
     * @param operationId Id devuelto por Question Service en el 202.
     * @return Operación procesada.
     */
    @GetMapping("/operations/{operationId}")
    public ProcessedOperation getOperation(@PathVariable String operationId) {
        return service.getOperation(operationId);
    }

    /**
     * @brief Traduce NotFoundException a 404.
     * @param e Excepción.
     * @return Cuerpo con el mensaje.
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(NotFoundException e) {
        return Map.of("status", 404, "message", e.getMessage(), "timestamp", Instant.now());
    }
}
