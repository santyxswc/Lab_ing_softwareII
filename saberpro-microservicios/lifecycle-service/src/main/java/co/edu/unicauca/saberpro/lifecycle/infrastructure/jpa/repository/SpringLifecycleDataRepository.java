/**
 * @file SpringLifecycleDataRepository.java
 * @brief Repositorio Spring Data de QuestionLifecycleRecord.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.repository;

import co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.record.QuestionLifecycleRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Implementación generada por Spring.
 */
public interface SpringLifecycleDataRepository extends JpaRepository<QuestionLifecycleRecord, Long> {
}
