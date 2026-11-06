/**
 * @file ProcessedOperationRecord.java
 * @brief Entidad JPA del registro de operaciones (tabla PROCESSED_OPERATIONS).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.record;

import co.edu.unicauca.saberpro.lifecycle.domain.model.OperationOutcome;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * @brief Una fila por cada operationId procesado.
 */
@Entity
@Table(name = "processed_operations")
public class ProcessedOperationRecord {

    @Id
    @Column(name = "operation_id", length = 36)
    private String operationId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OperationOutcome outcome;

    @Column(length = 500)
    private String detail;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    /** @brief Constructor vacío que exige JPA. */
    public ProcessedOperationRecord() {
    }

    /**
     * @brief Construye el registro.
     * @param operationId Id.
     * @param questionId  Pregunta.
     * @param outcome     Resultado.
     * @param detail      Detalle.
     * @param processedAt Momento.
     */
    public ProcessedOperationRecord(String operationId, Long questionId, OperationOutcome outcome,
                                    String detail, Instant processedAt) {
        this.operationId = operationId;
        this.questionId = questionId;
        this.outcome = outcome;
        this.detail = detail;
        this.processedAt = processedAt;
    }

    /** @return Id. */
    public String getOperationId() {
        return operationId;
    }

    /** @return Pregunta. */
    public Long getQuestionId() {
        return questionId;
    }

    /** @return Resultado. */
    public OperationOutcome getOutcome() {
        return outcome;
    }

    /** @return Detalle. */
    public String getDetail() {
        return detail;
    }

    /** @return Momento. */
    public Instant getProcessedAt() {
        return processedAt;
    }
}
