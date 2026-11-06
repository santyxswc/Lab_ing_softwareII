/**
 * @file QuestionController.java
 * @brief Endpoints REST de Question Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.rest;

import co.edu.unicauca.saberpro.question.application.QuestionService;
import co.edu.unicauca.saberpro.question.application.dto.SubmissionReceipt;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.infrastructure.rest.dto.CreateQuestionRequest;
import co.edu.unicauca.saberpro.question.infrastructure.rest.dto.QuestionResponse;
import co.edu.unicauca.saberpro.question.infrastructure.rest.dto.SubmitAcceptedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * @brief Adaptador HTTP de entrada.
 *
 * <ul>
 *   <li>{@code POST /questions} → 201 Created (o 400 si la validación falla).</li>
 *   <li>{@code POST /questions/{id}/submit} → 202 Accepted con operationId.</li>
 *   <li>{@code GET /questions} y {@code GET /questions/{id}} → consulta.</li>
 * </ul>
 * Los errores los traduce {@link RestExceptionHandler}.
 */
@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService service;

    /**
     * @brief Inyecta el caso de uso.
     * @param service Servicio de preguntas.
     */
    public QuestionController(QuestionService service) {
        this.service = service;
    }

    /**
     * @brief Crea una pregunta (Punto 2.1).
     * @param request Datos del reactivo.
     * @return 201 Created con la pregunta guardada y cabecera Location.
     */
    @PostMapping
    public ResponseEntity<QuestionResponse> create(@RequestBody CreateQuestionRequest request) {
        Question saved = service.create(request.toCommand());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(QuestionResponse.from(saved));
    }

    /**
     * @brief Envía una pregunta a revisión de forma asíncrona (Punto 2.2).
     * @param id Pregunta a enviar.
     * @return 202 Accepted con el operationId.
     */
    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmitAcceptedResponse> submit(@PathVariable Long id) {
        SubmissionReceipt receipt = service.submitForReview(id);
        return ResponseEntity.accepted().body(new SubmitAcceptedResponse(
                receipt.operationId(), receipt.questionId(),
                "Solicitud de revisión encolada. Lifecycle Service la procesará."));
    }

    /**
     * @brief Consulta una pregunta.
     * @param id Identificador.
     * @return 200 OK con la pregunta.
     */
    @GetMapping("/{id}")
    public QuestionResponse getById(@PathVariable Long id) {
        return QuestionResponse.from(service.getById(id));
    }

    /**
     * @brief Lista todas las preguntas.
     * @return 200 OK con la lista.
     */
    @GetMapping
    public List<QuestionResponse> findAll() {
        return service.findAll().stream().map(QuestionResponse::from).toList();
    }
}
