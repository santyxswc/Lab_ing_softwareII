/**
 * @file SpringOperationDataRepository.java
 * @brief Repositorio Spring Data de ProcessedOperationRecord.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.repository;

import co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.record.ProcessedOperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Implementación generada por Spring.
 */
public interface SpringOperationDataRepository extends JpaRepository<ProcessedOperationRecord, String> {
}
