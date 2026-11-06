/**
 * @file QuestionLifecycleRecord.java
 * @brief Entidad JPA del ciclo de vida (tabla QUESTION_LIFECYCLES).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.record;

import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * @brief Guarda el nombre del estado; el objeto estado se reconstruye con
 *        {@link QuestionStatus#toState()}.
 */
@Entity
@Table(name = "question_lifecycles")
public class QuestionLifecycleRecord {

    @Id
    @Column(name = "question_id")
    private Long questionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuestionStatus status;

    @Column(name = "last_operation_id", length = 36)
    private String lastOperationId;

    @Column(name = "updated_at")
    private Instant updatedAt;

    /** @brief Constructor vacío que exige JPA. */
    public QuestionLifecycleRecord() {
    }

    /**
     * @brief Construye el registro.
     * @param questionId      Pregunta.
     * @param status          Estado.
     * @param lastOperationId Última operación.
     * @param updatedAt       Último cambio.
     */
    public QuestionLifecycleRecord(Long questionId, QuestionStatus status, String lastOperationId, Instant updatedAt) {
        this.questionId = questionId;
        this.status = status;
        this.lastOperationId = lastOperationId;
        this.updatedAt = updatedAt;
    }

    /** @return Pregunta. */
    public Long getQuestionId() {
        return questionId;
    }

    /** @return Estado. */
    public QuestionStatus getStatus() {
        return status;
    }

    /** @return Última operación. */
    public String getLastOperationId() {
        return lastOperationId;
    }

    /** @return Último cambio. */
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
