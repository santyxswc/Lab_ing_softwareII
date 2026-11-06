/**
 * @file QuestionRepository.java
 * @brief Puerto de salida para persistir preguntas.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.port;

import co.edu.unicauca.saberpro.question.domain.model.Question;

import java.util.List;
import java.util.Optional;

/**
 * @brief Contrato de persistencia que conoce el dominio.
 *
 * Solo habla de objetos de dominio; la implementación
 * ({@code QuestionRepositoryJpaImpl}) se encarga de JPA.
 */
public interface QuestionRepository {

    /**
     * @brief Guarda o actualiza una pregunta.
     * @param question Pregunta de dominio.
     * @return Pregunta guardada, con id asignado.
     */
    Question save(Question question);

    /**
     * @brief Busca una pregunta por id.
     * @param id Identificador.
     * @return La pregunta, o vacío si no existe.
     */
    Optional<Question> findById(Long id);

    /**
     * @brief Lista todas las preguntas.
     * @return Preguntas guardadas.
     */
    List<Question> findAll();
}
