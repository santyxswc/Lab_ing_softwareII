/**
 * @file QuestionRecord.java
 * @brief Entidad JPA que representa una pregunta en la tabla QUESTIONS.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.jpa.record;

import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Modelo relacional de la pregunta (Punto 3.2).
 *
 * Solo existe en la capa de infraestructura. Se relaciona 1 a N con
 * {@link AnswerOptionRecord} usando {@code cascade = CascadeType.ALL}: al
 * guardar la pregunta se guardan sus opciones.
 */
@Entity
@Table(name = "questions")
public class QuestionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String statement;

    @Column(nullable = false, length = 2000)
    private String justification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuestionStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("label ASC")
    private List<AnswerOptionRecord> options = new ArrayList<>();

    /** @brief Constructor vacío que exige JPA. */
    public QuestionRecord() {
    }

    /**
     * @brief Agrega una opción y mantiene la relación en ambos sentidos.
     * @param option Opción a enlazar con esta pregunta.
     */
    public void addOption(AnswerOptionRecord option) {
        option.setQuestion(this);
        options.add(option);
    }

    /** @return Identificador. */
    public Long getId() {
        return id;
    }

    /** @param id Identificador. */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return Enunciado. */
    public String getStatement() {
        return statement;
    }

    /** @param statement Enunciado. */
    public void setStatement(String statement) {
        this.statement = statement;
    }

    /** @return Justificación. */
    public String getJustification() {
        return justification;
    }

    /** @param justification Justificación. */
    public void setJustification(String justification) {
        this.justification = justification;
    }

    /** @return Estado. */
    public QuestionStatus getStatus() {
        return status;
    }

    /** @param status Estado. */
    public void setStatus(QuestionStatus status) {
        this.status = status;
    }

    /** @return Fecha de creación. */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt Fecha de creación. */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /** @return Opciones (lista gestionada por JPA). */
    public List<AnswerOptionRecord> getOptions() {
        return options;
    }
}
