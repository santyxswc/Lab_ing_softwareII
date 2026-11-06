/**
 * @file QuestionBuilder.java
 * @brief Implementación concreta del patrón Builder para reactivos.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.builder;

import co.edu.unicauca.saberpro.question.domain.exception.InvalidQuestionException;
import co.edu.unicauca.saberpro.question.domain.model.AnswerOption;
import co.edu.unicauca.saberpro.question.domain.model.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @brief Ensambla paso a paso una pregunta de 4 opciones y 1 respuesta correcta.
 *
 * {@link #build()} revisa todas las reglas antes de crear el objeto y
 * reporta todos los errores juntos en una {@link InvalidQuestionException}:
 * <ul>
 *   <li>Enunciado y justificación no vacíos.</li>
 *   <li>Exactamente {@value IQuestionBuilder#REQUIRED_OPTIONS} opciones.</li>
 *   <li>Letras no vacías y sin repetir; textos no vacíos.</li>
 *   <li>Respuesta correcta indicada y existente entre las opciones.</li>
 * </ul>
 * La longitud mínima de la justificación NO se valida aquí: es
 * responsabilidad de Validation Service (Punto 2.1).
 *
 * Cada instancia sirve para construir una sola pregunta.
 */
public class QuestionBuilder implements IQuestionBuilder {

    /** Opción todavía sin marcar como correcta. */
    private record PendingOption(String label, String text) {
    }

    private String statement;
    private String justification;
    private String correctLabel;
    private final List<PendingOption> options = new ArrayList<>();

    @Override
    public IQuestionBuilder withStatement(String statement) {
        this.statement = statement;
        return this;
    }

    @Override
    public IQuestionBuilder withJustification(String justification) {
        this.justification = justification;
        return this;
    }

    @Override
    public IQuestionBuilder withOption(String label, String text) {
        options.add(new PendingOption(normalize(label), text));
        return this;
    }

    @Override
    public IQuestionBuilder withCorrectAnswer(String label) {
        this.correctLabel = normalize(label);
        return this;
    }

    @Override
    public Question build() {
        List<String> errors = validate();
        if (!errors.isEmpty()) {
            throw new InvalidQuestionException(errors);
        }
        List<AnswerOption> answerOptions = options.stream()
                .map(o -> new AnswerOption(null, o.label(), o.text().strip(), o.label().equals(correctLabel)))
                .toList();
        return Question.newDraft(statement.strip(), justification.strip(), answerOptions);
    }

    /**
     * @brief Revisa todas las reglas estructurales.
     * @return Lista de errores; vacía si el reactivo es válido.
     */
    private List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (isBlank(statement)) {
            errors.add("El enunciado es obligatorio.");
        }
        if (isBlank(justification)) {
            errors.add("La justificación es obligatoria.");
        }
        if (options.size() != REQUIRED_OPTIONS) {
            errors.add("Debe tener exactamente " + REQUIRED_OPTIONS + " opciones (tiene " + options.size() + ").");
        }
        Set<String> labels = new HashSet<>();
        for (PendingOption option : options) {
            if (isBlank(option.label())) {
                errors.add("Toda opción debe tener una letra.");
            } else if (!labels.add(option.label())) {
                errors.add("La opción " + option.label() + " está repetida.");
            }
            if (isBlank(option.text())) {
                errors.add("La opción " + option.label() + " no tiene texto.");
            }
        }
        if (isBlank(correctLabel)) {
            errors.add("Debe indicar la respuesta correcta.");
        } else if (!labels.contains(correctLabel)) {
            errors.add("La respuesta correcta " + correctLabel + " no corresponde a ninguna opción.");
        }
        return errors;
    }

    private static String normalize(String label) {
        return label == null ? null : label.strip().toUpperCase(Locale.ROOT);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
