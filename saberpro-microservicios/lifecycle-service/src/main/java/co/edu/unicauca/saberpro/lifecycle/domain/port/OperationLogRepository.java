/**
 * @file OperationLogRepository.java
 * @brief Puerto de persistencia del registro de operaciones.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.port;

import co.edu.unicauca.saberpro.lifecycle.domain.model.ProcessedOperation;

import java.util.Optional;

/**
 * @brief Historial de operaciones procesadas.
 */
public interface OperationLogRepository {

    /**
     * @brief Registra una operación.
     * @param operation Operación procesada.
     */
    void save(ProcessedOperation operation);

    /**
     * @brief Busca una operación.
     * @param operationId Id.
     * @return La operación, o vacío.
     */
    Optional<ProcessedOperation> findById(String operationId);

    /**
     * @brief Indica si ya se procesó (idempotencia).
     * @param operationId Id.
     * @return {@code true} si existe.
     */
    boolean exists(String operationId);
}
