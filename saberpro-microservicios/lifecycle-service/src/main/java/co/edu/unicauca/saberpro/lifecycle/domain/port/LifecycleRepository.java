/**
 * @file LifecycleRepository.java
 * @brief Puerto de persistencia de ciclos de vida.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.port;

import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;

import java.util.Optional;

/**
 * @brief Contrato que conoce el dominio; lo implementa un adaptador JPA.
 */
public interface LifecycleRepository {

    /**
     * @brief Guarda o actualiza.
     * @param lifecycle Ciclo de vida.
     * @return El ciclo de vida guardado.
     */
    QuestionLifecycle save(QuestionLifecycle lifecycle);

    /**
     * @brief Busca por pregunta.
     * @param questionId Pregunta.
     * @return El ciclo de vida, o vacío.
     */
    Optional<QuestionLifecycle> findByQuestionId(Long questionId);
}
