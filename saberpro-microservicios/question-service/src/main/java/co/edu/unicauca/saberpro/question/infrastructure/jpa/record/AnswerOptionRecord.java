/**
 * @file AnswerOptionRecord.java
 * @brief Entidad JPA de una opción de respuesta (tabla ANSWER_OPTIONS).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.jpa.record;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @brief Lado "muchos" de la relación con {@link QuestionRecord}.
 *
 * La columna {@code question_id} es la llave foránea.
 */
@Entity
@Table(name = "answer_options")
public class AnswerOptionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 5)
    private String label;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(nullable = false)
    private boolean correct;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionRecord question;

    /** @brief Constructor vacío que exige JPA. */
    public AnswerOptionRecord() {
    }

    /**
     * @brief Construye una opción.
     * @param id      Identificador o {@code null} si es nueva.
     * @param label   Letra.
     * @param text    Texto.
     * @param correct Si es la correcta.
     */
    public AnswerOptionRecord(Long id, String label, String text, boolean correct) {
        this.id = id;
        this.label = label;
        this.text = text;
        this.correct = correct;
    }

    /** @return Identificador. */
    public Long getId() {
        return id;
    }

    /** @return Letra. */
    public String getLabel() {
        return label;
    }

    /** @return Texto. */
    public String getText() {
        return text;
    }

    /** @return {@code true} si es la correcta. */
    public boolean isCorrect() {
        return correct;
    }

    /** @return Pregunta dueña. */
    public QuestionRecord getQuestion() {
        return question;
    }

    /** @param question Pregunta dueña (la asigna {@link QuestionRecord#addOption}). */
    void setQuestion(QuestionRecord question) {
        this.question = question;
    }
}
