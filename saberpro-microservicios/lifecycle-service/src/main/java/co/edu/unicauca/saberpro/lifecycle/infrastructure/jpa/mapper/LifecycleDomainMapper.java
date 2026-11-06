/**
 * @file LifecycleDomainMapper.java
 * @brief Mapeador bidireccional entre dominio y entidades JPA de Lifecycle Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.mapper;

import co.edu.unicauca.saberpro.lifecycle.domain.model.ProcessedOperation;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.record.ProcessedOperationRecord;
import co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.record.QuestionLifecycleRecord;
import org.springframework.stereotype.Component;

/**
 * @brief Mantiene el dominio libre de anotaciones JPA.
 */
@Component
public class LifecycleDomainMapper {

    /**
     * @brief Dominio → JPA.
     * @param lifecycle Ciclo de vida.
     * @return Entidad.
     */
    public QuestionLifecycleRecord toRecord(QuestionLifecycle lifecycle) {
        return new QuestionLifecycleRecord(lifecycle.getQuestionId(), lifecycle.getStatus(),
                lifecycle.getLastOperationId(), lifecycle.getUpdatedAt());
    }

    /**
     * @brief JPA → dominio.
     * @param record Entidad.
     * @return Ciclo de vida con su objeto estado.
     */
    public QuestionLifecycle toDomain(QuestionLifecycleRecord record) {
        return QuestionLifecycle.restore(record.getQuestionId(), record.getStatus(),
                record.getLastOperationId(), record.getUpdatedAt());
    }

    /**
     * @brief Dominio → JPA.
     * @param op Operación.
     * @return Entidad.
     */
    public ProcessedOperationRecord toRecord(ProcessedOperation op) {
        return new ProcessedOperationRecord(op.operationId(), op.questionId(), op.outcome(),
                op.detail(), op.processedAt());
    }

    /**
     * @brief JPA → dominio.
     * @param r Entidad.
     * @return Operación.
     */
    public ProcessedOperation toDomain(ProcessedOperationRecord r) {
        return new ProcessedOperation(r.getOperationId(), r.getQuestionId(), r.getOutcome(),
                r.getDetail(), r.getProcessedAt());
    }
}
