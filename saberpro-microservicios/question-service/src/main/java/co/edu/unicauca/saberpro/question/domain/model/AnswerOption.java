/**
 * @file AnswerOption.java
 * @brief Opción de respuesta de una pregunta de selección múltiple.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.model;

import java.util.Objects;

/**
 * @brief Una de las cuatro opciones (A, B, C, D) de un reactivo.
 *
 * Objeto de dominio en Java puro: sin anotaciones de persistencia.
 */
public final class AnswerOption {

    private final Long id;
    private final String label;
    private final String text;
    private final boolean correct;

    /**
     * @brief Construye una opción.
     * @param id      Identificador persistido, o {@code null} si es nueva.
     * @param label   Letra de la opción (por ejemplo, "A").
     * @param text    Texto que ve el estudiante.
     * @param correct {@code true} si es la respuesta correcta.
     */
    public AnswerOption(Long id, String label, String text, boolean correct) {
        this.id = id;
        this.label = Objects.requireNonNull(label, "label");
        this.text = Objects.requireNonNull(text, "text");
        this.correct = correct;
    }

    /** @return Identificador persistido o {@code null}. */
    public Long getId() {
        return id;
    }

    /** @return Letra de la opción. */
    public String getLabel() {
        return label;
    }

    /** @return Texto de la opción. */
    public String getText() {
        return text;
    }

    /** @return {@code true} si es la respuesta correcta. */
    public boolean isCorrect() {
        return correct;
    }
}
