/**
 * @file PreguntaRepositoryPort.java
 * @brief Puerto de Salida (Driven / Outbound) para la persistencia del Agregado Pregunta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.ports.outbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de Salida (Driven) para la persistencia de reactivos.
 * Contiene operaciones puras del dominio, sin acoplamiento a tecnologías
 * de almacenamiento o frameworks.
 */
public interface PreguntaRepositoryPort {

    /**
     * Guarda o actualiza un reactivo en el medio de almacenamiento configurado.
     *
     * @param pregunta Agregado a persistir.
     * @return El Agregado persistido con su identificador asignado.
     */
    Pregunta save(Pregunta pregunta);

    /**
     * Busca un reactivo por su identificador único.
     *
     * @param id Identificador del reactivo.
     * @return Optional con el Agregado reconstruido, o vacío si no existe.
     */
    Optional<Pregunta> findById(Long id);

    /**
     * Recupera todos los reactivos registrados.
     *
     * @return Lista de reactivos almacenados.
     */
    List<Pregunta> findAll();
}

