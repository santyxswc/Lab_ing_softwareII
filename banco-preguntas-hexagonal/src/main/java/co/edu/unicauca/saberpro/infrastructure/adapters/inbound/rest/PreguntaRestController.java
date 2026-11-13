/**
 * @file PreguntaRestController.java
 * @brief Adaptador de entrada (Driving / Inbound) REST para la gestión de preguntas.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.inbound.rest;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.inbound.ConsultarPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.infrastructure.adapters.inbound.rest.dto.CrearPreguntaRequest;
import co.edu.unicauca.saberpro.infrastructure.adapters.inbound.rest.dto.PreguntaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Adaptador primario (driving) HTTP/REST que expone los casos de uso
 * a clientes externos sin acoplarlos al dominio interno.
 */
@RestController
@RequestMapping("/api/preguntas")
public class PreguntaRestController {

    private final CrearPreguntaUseCase crearPreguntaUseCase;
    private final ConsultarPreguntaUseCase consultarPreguntaUseCase;

    public PreguntaRestController(CrearPreguntaUseCase crearPreguntaUseCase,
                                  ConsultarPreguntaUseCase consultarPreguntaUseCase) {
        this.crearPreguntaUseCase = Objects.requireNonNull(crearPreguntaUseCase, "crearPreguntaUseCase no puede ser nulo.");
        this.consultarPreguntaUseCase = Objects.requireNonNull(consultarPreguntaUseCase, "consultarPreguntaUseCase no puede ser nulo.");
    }

    /**
     * Crea un nuevo reactivo y lo envía a revisión (transición BORRADOR -> PENDIENTE_REVISION).
     *
     * @param request Datos del reactivo y sus 4 opciones.
     * @return 201 Created con el reactivo persistido.
     */
    @PostMapping
    public ResponseEntity<PreguntaResponse> crearPregunta(@Valid @RequestBody CrearPreguntaRequest request) {
        Pregunta pregunta = crearPreguntaUseCase.crearPregunta(request.toCommand());
        PreguntaResponse response = PreguntaResponse.fromDomain(pregunta);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(pregunta.getId())
            .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Consulta un reactivo por su identificador.
     *
     * @param id Identificador numérico.
     * @return 200 OK con el reactivo o 404 Not Found si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PreguntaResponse> obtenerPorId(@PathVariable Long id) {
        return consultarPreguntaUseCase.obtenerPorId(id)
            .map(p -> ResponseEntity.ok(PreguntaResponse.fromDomain(p)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Lista todos los reactivos almacenados en el repositorio activo.
     *
     * @return 200 OK con la lista de reactivos.
     */
    @GetMapping
    public ResponseEntity<List<PreguntaResponse>> obtenerTodas() {
        List<PreguntaResponse> lista = consultarPreguntaUseCase.obtenerTodas().stream()
            .map(PreguntaResponse::fromDomain)
            .toList();
        return ResponseEntity.ok(lista);
    }
}

