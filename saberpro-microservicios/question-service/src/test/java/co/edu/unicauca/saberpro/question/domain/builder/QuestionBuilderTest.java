/**
 * @file QuestionBuilderTest.java
 * @brief Pruebas del patrón Builder (Punto 1.1).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.builder;

import co.edu.unicauca.saberpro.question.domain.exception.InvalidQuestionException;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @brief Verifica la construcción fluida y la validación de build().
 */
class QuestionBuilderTest {

    /** @return Builder con las 4 opciones y la respuesta correcta B. */
    private static IQuestionBuilder valid() {
        return new QuestionBuilder()
                .withStatement("¿Cuál es la capital de Colombia?")
                .withJustification("Bogotá es la capital desde 1819.")
                .withOption("A", "Medellín")
                .withOption("b", "Bogotá")
                .withOption("C", "Cali")
                .withOption("D", "Popayán")
                .withCorrectAnswer("B");
    }

    /** @brief Reactivo válido: 4 opciones, B correcta, estado DRAFT, sin id. */
    @Test
    void buildsValidQuestionInDraft() {
        Question q = valid().build();
        assertEquals(4, q.getOptions().size());
        assertEquals("B", q.getCorrectOption().getLabel());
        assertEquals(1, q.getOptions().stream().filter(o -> o.isCorrect()).count());
        assertEquals(QuestionStatus.DRAFT, q.getStatus());
        assertNull(q.getId());
    }

    /** @brief Menos de 4 opciones se rechaza. */
    @Test
    void rejectsThreeOptions() {
        IQuestionBuilder b = new QuestionBuilder()
                .withStatement("Enunciado").withJustification("Justificación")
                .withOption("A", "1").withOption("B", "2").withOption("C", "3")
                .withCorrectAnswer("A");
        InvalidQuestionException e = assertThrows(InvalidQuestionException.class, b::build);
        assertTrue(e.getErrors().stream().anyMatch(m -> m.contains("exactamente 4")));
    }

    /** @brief Más de 4 opciones se rechaza. */
    @Test
    void rejectsFiveOptions() {
        assertThrows(InvalidQuestionException.class, () -> valid().withOption("E", "Pasto").build());
    }

    /** @brief La respuesta correcta debe existir entre las opciones. */
    @Test
    void rejectsUnknownCorrectAnswer() {
        assertThrows(InvalidQuestionException.class, () -> valid().withCorrectAnswer("Z").build());
    }

    /** @brief Sin respuesta correcta se rechaza. */
    @Test
    void rejectsMissingCorrectAnswer() {
        assertThrows(InvalidQuestionException.class, () -> valid().withCorrectAnswer(null).build());
    }

    /** @brief Letras repetidas se rechazan. */
    @Test
    void rejectsDuplicatedLabels() {
        IQuestionBuilder b = new QuestionBuilder()
                .withStatement("Enunciado").withJustification("Justificación")
                .withOption("A", "1").withOption("A", "2").withOption("C", "3").withOption("D", "4")
                .withCorrectAnswer("A");
        assertThrows(InvalidQuestionException.class, b::build);
    }

    /** @brief Se reportan todos los errores a la vez. */
    @Test
    void reportsAllErrorsTogether() {
        InvalidQuestionException e = assertThrows(InvalidQuestionException.class,
                () -> new QuestionBuilder().build());
        assertTrue(e.getErrors().size() >= 4);
    }
}
