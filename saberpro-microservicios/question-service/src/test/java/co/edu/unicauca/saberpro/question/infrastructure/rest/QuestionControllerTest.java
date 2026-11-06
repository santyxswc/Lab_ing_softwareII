/**
 * @file QuestionControllerTest.java
 * @brief Pruebas de los códigos HTTP del controlador (201, 400, 202, 404).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.rest;

import co.edu.unicauca.saberpro.question.application.QuestionService;
import co.edu.unicauca.saberpro.question.application.dto.SubmissionReceipt;
import co.edu.unicauca.saberpro.question.application.exception.ValidationRejectedException;
import co.edu.unicauca.saberpro.question.domain.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.question.domain.exception.QuestionNotFoundException;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @brief Prueba solo la capa web; el caso de uso es un mock.
 */
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    private static final String BODY = """
            {"statement":"¿2 + 2?","justification":"Dos más dos es cuatro siempre.",
             "options":[{"label":"A","text":"3"},{"label":"B","text":"4"},
                        {"label":"C","text":"5"},{"label":"D","text":"6"}],
             "correctAnswer":"B"}
            """;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private QuestionService service;

    private static Question saved() {
        Question draft = new QuestionBuilder()
                .withStatement("¿2 + 2?").withJustification("Dos más dos es cuatro siempre.")
                .withOption("A", "3").withOption("B", "4").withOption("C", "5").withOption("D", "6")
                .withCorrectAnswer("B").build();
        return Question.restore(1L, draft.getStatement(), draft.getJustification(), draft.getOptions(),
                QuestionStatus.DRAFT, Instant.now());
    }

    /** @brief Creación exitosa → 201 con Location y estado DRAFT. */
    @Test
    void createReturns201() throws Exception {
        when(service.create(any())).thenReturn(saved());
        mvc.perform(post("/questions").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/questions/1"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.correctAnswer").value("B"));
    }

    /** @brief Justificación rechazada → 400. */
    @Test
    void createReturns400WhenRejected() throws Exception {
        when(service.create(any())).thenThrow(new ValidationRejectedException("muy corta"));
        mvc.perform(post("/questions").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("muy corta"));
    }

    /** @brief Envío a revisión → 202 con operationId. */
    @Test
    void submitReturns202() throws Exception {
        when(service.submitForReview(1L)).thenReturn(new SubmissionReceipt("op-123", 1L));
        mvc.perform(post("/questions/1/submit"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.operationId").value("op-123"));
    }

    /** @brief Pregunta inexistente → 404. */
    @Test
    void submitReturns404() throws Exception {
        when(service.submitForReview(9L)).thenThrow(new QuestionNotFoundException(9L));
        mvc.perform(post("/questions/9/submit")).andExpect(status().isNotFound());
    }
}
