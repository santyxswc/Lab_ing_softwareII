/**
 * @file ValidationController.java
 * @brief Endpoint REST POST /validate.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.validation.infrastructure.rest;

import co.edu.unicauca.saberpro.validation.application.JustificationValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @brief Expone la validación de justificaciones.
 *
 * Siempre responde HTTP 200: el veredicto viaja en el campo {@code valid}.
 * Así Question Service distingue "justificación inválida" (valid=false)
 * de "el servicio falló" (error HTTP o de red).
 */
@RestController
public class ValidationController {

    private final JustificationValidationService service;

    /**
     * @brief Inyecta el caso de uso.
     * @param service Servicio de validación.
     */
    public ValidationController(JustificationValidationService service) {
        this.service = service;
    }

    /**
     * @brief Valida la justificación recibida.
     * @param request Cuerpo con la justificación.
     * @return 200 OK con el veredicto.
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponse> validate(@RequestBody ValidationRequest request) {
        return ResponseEntity.ok(ValidationResponse.from(service.validate(request.justification())));
    }
}
