/**
 * @file ConsultarPreguntaUseCase.java
 * @brief Puerto de Entrada (Driving / Inbound) para la consulta de preguntas.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.ports.inbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de Entrada para operaciones de consulta sobre el banco de preguntas.
 */
public interface ConsultarPreguntaUseCase {

    /**
     * Consulta una pregunta por su identificador único.
     *
     * @param id Identificador de la pregunta.
     * @return Optional con la pregunta encontrada o vacío.
     */
    Optional<Pregunta> obtenerPorId(Long id);

    /**
     * Lista todas las preguntas existentes.
     *
     * @return Lista de preguntas.
     */
    List<Pregunta> obtenerTodas();
}

