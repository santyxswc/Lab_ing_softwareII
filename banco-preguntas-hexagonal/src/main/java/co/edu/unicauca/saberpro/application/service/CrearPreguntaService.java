/**
 * @file CrearPreguntaService.java
 * @brief Servicio de aplicación que orquesta la creación y validación de reactivos.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.application.service;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.domain.model.Enunciado;
import co.edu.unicauca.saberpro.domain.model.Justificacion;
import co.edu.unicauca.saberpro.domain.model.OpcionRespuesta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.inbound.ConsultarPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaCommand;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Caso de uso de aplicación que implementa los puertos de entrada {@link CrearPreguntaUseCase}
 * y {@link ConsultarPreguntaUseCase}.
 *
 * No conoce ningún detalle de persistencia concreto (JPA o memoria), solo interactúa
 * con la abstracción del puerto de salida {@link PreguntaRepositoryPort}.
 */
@Service
public class CrearPreguntaService implements CrearPreguntaUseCase, ConsultarPreguntaUseCase {

    private final PreguntaRepositoryPort repositoryPort;

    public CrearPreguntaService(PreguntaRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "El puerto de repositorio no puede ser nulo.");
    }

    @Override
    public Pregunta crearPregunta(CrearPreguntaCommand command) {
        if (command == null) {
            throw new ReglaNegocioException("El comando de creación no puede ser nulo.");
        }

        // 1. Construir Objetos de Valor (validan no nulidad, tamaño y consistencia)
        Enunciado enunciado = new Enunciado(command.enunciado());
        Justificacion justificacion = new Justificacion(command.justificacion());

        List<OpcionRespuesta> opciones = new ArrayList<>();
        if (command.opciones() != null) {
            for (CrearPreguntaCommand.OpcionItem item : command.opciones()) {
                opciones.add(new OpcionRespuesta(item.texto(), item.esCorrecta()));
            }
        }

        // 2. Instanciar el Agregado en estado inicial BORRADOR
        Pregunta pregunta = Pregunta.crearBorrador(enunciado, justificacion, opciones);

        // 3. Ejecutar la transición de ciclo de vida validando las invariantes de negocio
        pregunta.enviarARevision();

        // 4. Persistir a través del puerto de salida
        return repositoryPort.save(pregunta);
    }

    @Override
    public Optional<Pregunta> obtenerPorId(Long id) {
        return repositoryPort.findById(id);
    }

    @Override
    public List<Pregunta> obtenerTodas() {
        return repositoryPort.findAll();
    }
}

