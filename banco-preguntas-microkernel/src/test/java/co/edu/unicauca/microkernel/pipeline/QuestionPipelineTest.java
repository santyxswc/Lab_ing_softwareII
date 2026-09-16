package co.edu.unicauca.microkernel.pipeline;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionPipeline;
import co.edu.unicauca.microkernel.pipeline.filters.ClassificationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.CorrectAnswerValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.OptionsValidationFilter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del patrón Tuberías y Filtros: cada filtro se
 * prueba de forma aislada, y luego se prueba el pipeline completo
 * ensamblado (igual a como lo arma MultipleChoiceQuestionPlugin).
 */
public class QuestionPipelineTest {

    private static final List<String> OPCIONES_VALIDAS = Arrays.asList(
            "Single Responsibility", "Open Closed", "Liskov", "Interface Segregation");

    private QuestionRequest requestValida() {
        return new QuestionRequest(
                "Pregunta SOLID",
                "¿Qué representa la S en SOLID?",
                "MULTIPLE_CHOICE",
                "Arquitectura de software",
                OPCIONES_VALIDAS,
                "Single Responsibility"
        );
    }

    // ---------- ContentValidationFilter ----------

    @Test
    public void testContentValidationFilterInvalido() {
        ContentValidationFilter filter = new ContentValidationFilter();
        QuestionRequest requestInvalido = new QuestionRequest("", "",
                "MULTIPLE_CHOICE", "Arquitectura de software", null, "");
        assertFalse(filter.process(requestInvalido));
    }

    @Test
    public void testContentValidationFilterValido() {
        ContentValidationFilter filter = new ContentValidationFilter();
        assertTrue(filter.process(requestValida()));
    }

    // ---------- OptionsValidationFilter ----------

    @Test
    public void testOptionsValidationFilterConCuatroOpciones() {
        OptionsValidationFilter filter = new OptionsValidationFilter();
        assertTrue(filter.process(requestValida()));
    }

    @Test
    public void testOptionsValidationFilterConMenosDeCuatroOpciones() {
        OptionsValidationFilter filter = new OptionsValidationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "Arquitectura de software",
                Arrays.asList("A", "B"), "A");
        assertFalse(filter.process(request));
    }

    @Test
    public void testOptionsValidationFilterSinOpciones() {
        OptionsValidationFilter filter = new OptionsValidationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "Arquitectura de software",
                null, "A");
        assertFalse(filter.process(request));
    }

    // ---------- ClassificationFilter ----------

    @Test
    public void testClassificationFilterValida() {
        ClassificationFilter filter = new ClassificationFilter();
        assertTrue(filter.process(requestValida()));
    }

    @Test
    public void testClassificationFilterInvalida() {
        ClassificationFilter filter = new ClassificationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "Cocina Internacional",
                OPCIONES_VALIDAS, "Single Responsibility");
        assertFalse(filter.process(request));
    }

    @Test
    public void testClassificationFilterEsCaseInsensitive() {
        ClassificationFilter filter = new ClassificationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "ARQUITECTURA DE SOFTWARE",
                OPCIONES_VALIDAS, "Single Responsibility");
        assertTrue(filter.process(request));
    }

    // ---------- CorrectAnswerValidationFilter ----------

    @Test
    public void testCorrectAnswerValidationFilterRespuestaPresente() {
        CorrectAnswerValidationFilter filter = new CorrectAnswerValidationFilter();
        assertTrue(filter.process(requestValida()));
    }

    @Test
    public void testCorrectAnswerValidationFilterRespuestaAusente() {
        CorrectAnswerValidationFilter filter = new CorrectAnswerValidationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "Arquitectura de software",
                OPCIONES_VALIDAS, "Respuesta que no existe en las opciones");
        assertFalse(filter.process(request));
    }

    // ---------- QuestionPipeline completo ----------

    @Test
    public void testPipelineCompletoConSolicitudValida() {
        QuestionPipeline pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());

        assertTrue(pipeline.execute(requestValida()));
    }

    @Test
    public void testPipelineCompletoSeDetieneEnElPrimerFiltroQueFalla() {
        QuestionPipeline pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());

        // Título vacío: debe fallar en el primer filtro (Content) y
        // no debe siquiera evaluar los demás.
        QuestionRequest requestInvalida = new QuestionRequest(
                "", "Contenido", "MULTIPLE_CHOICE", "Arquitectura de software",
                OPCIONES_VALIDAS, "Single Responsibility");

        assertFalse(pipeline.execute(requestInvalida));
    }
}
