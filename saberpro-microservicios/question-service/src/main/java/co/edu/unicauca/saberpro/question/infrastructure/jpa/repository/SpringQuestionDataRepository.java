/**
 * @file SpringQuestionDataRepository.java
 * @brief Repositorio Spring Data JPA sobre QuestionRecord.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.jpa.repository;

import co.edu.unicauca.saberpro.question.infrastructure.jpa.record.QuestionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Spring genera la implementación en tiempo de ejecución.
 *
 * Solo lo usa {@link QuestionRepositoryJpaImpl}; el resto de la aplicación
 * habla con el puerto de dominio {@code QuestionRepository}.
 */
public interface SpringQuestionDataRepository extends JpaRepository<QuestionRecord, Long> {
}
