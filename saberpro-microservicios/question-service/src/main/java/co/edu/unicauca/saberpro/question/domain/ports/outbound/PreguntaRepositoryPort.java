/**
 * @file PreguntaRepositoryPort.java
 * @brief Puerto de Salida (Driven / Outbound) para la persistencia de reactivos.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.ports.outbound;

import co.edu.unicauca.saberpro.question.domain.model.Question;

import java.util.List;
import java.util.Optional;

/**
 * @brief Puerto de Salida (Driven) para persistir reactivos.
 *
 * Contiene solo operaciones puras del dominio; no conoce JPA ni memoria. Sus
 * implementaciones son adaptadores intercambiables ({@code JpaQuestionAdapter}
 * y {@code InMemoryQuestionAdapter}).
 */
public interface PreguntaRepositoryPort {

    /**
     * @brief Guarda o actualiza un reactivo.
     * @param question Agregado a persistir.
     * @return El reactivo persistido, con id asignado.
     */
    Question save(Question question);

    /**
     * @brief Busca un reactivo por su identificador.
     * @param id Identificador.
     * @return El reactivo, o vacío si no existe.
     */
    Optional<Question> findById(Long id);

    /**
     * @brief Recupera todos los reactivos.
     * @return Reactivos almacenados.
     */
    List<Question> findAll();
}
