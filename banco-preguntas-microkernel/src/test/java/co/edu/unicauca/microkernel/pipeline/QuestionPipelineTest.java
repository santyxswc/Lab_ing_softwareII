/**
 * @file QuestionPipelineTest.java
 * @brief Pruebas de los filtros y del pipeline.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
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
 * @brief Pruebas del patrón tuberías y filtros.
 *
 * Cada filtro se prueba por separado y luego el pipeline completo, armado igual que en MultipleChoiceQuestionPlugin.
 */
public class QuestionPipelineTest {

    /** Cuatro opciones válidas de ejemplo. */
    private static final List<String> OPCIONES_VALIDAS = Arrays.asList(
            "Single Responsibility", "Open Closed", "Liskov", "Interface Segregation");

    /**
     * @brief Solicitud que cumple todas las reglas.
     * @return Solicitud válida
     */
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

    /**
     * @brief Título y contenido vacíos no pasan.
     */
    @Test
    public void testContentValidationFilterInvalido() {
        ContentValidationFilter filter = new ContentValidationFilter();
        QuestionRequest requestInvalido = new QuestionRequest("", "",
                "MULTIPLE_CHOICE", "Arquitectura de software", null, "");
        assertFalse(filter.process(requestInvalido));
    }

    /**
     * @brief Título y contenido completos pasan.
     */
    @Test
    public void testContentValidationFilterValido() {
        ContentValidationFilter filter = new ContentValidationFilter();
        assertTrue(filter.process(requestValida()));
    }

    /**
     * @brief Cuatro opciones pasan.
     */
    @Test
    public void testOptionsValidationFilterConCuatroOpciones() {
        OptionsValidationFilter filter = new OptionsValidationFilter();
        assertTrue(filter.process(requestValida()));
    }

    /**
     * @brief Dos opciones no pasan.
     */
    @Test
    public void testOptionsValidationFilterConMenosDeCuatroOpciones() {
        OptionsValidationFilter filter = new OptionsValidationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "Arquitectura de software",
                Arrays.asList("A", "B"), "A");
        assertFalse(filter.process(request));
    }

    /**
     * @brief Sin opciones no pasa.
     */
    @Test
    public void testOptionsValidationFilterSinOpciones() {
        OptionsValidationFilter filter = new OptionsValidationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "Arquitectura de software",
                null, "A");
        assertFalse(filter.process(request));
    }

    /**
     * @brief Un área de la lista pasa.
     */
    @Test
    public void testClassificationFilterValida() {
        ClassificationFilter filter = new ClassificationFilter();
        assertTrue(filter.process(requestValida()));
    }

    /**
     * @brief Un área fuera de la lista no pasa.
     */
    @Test
    public void testClassificationFilterInvalida() {
        ClassificationFilter filter = new ClassificationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "Cocina Internacional",
                OPCIONES_VALIDAS, "Single Responsibility");
        assertFalse(filter.process(request));
    }

    /**
     * @brief El área se compara sin distinguir mayúsculas.
     */
    @Test
    public void testClassificationFilterEsCaseInsensitive() {
        ClassificationFilter filter = new ClassificationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "ARQUITECTURA DE SOFTWARE",
                OPCIONES_VALIDAS, "Single Responsibility");
        assertTrue(filter.process(request));
    }

    /**
     * @brief Una respuesta que está entre las opciones pasa.
     */
    @Test
    public void testCorrectAnswerValidationFilterRespuestaPresente() {
        CorrectAnswerValidationFilter filter = new CorrectAnswerValidationFilter();
        assertTrue(filter.process(requestValida()));
    }

    /**
     * @brief Una respuesta que no está entre las opciones no pasa.
     */
    @Test
    public void testCorrectAnswerValidationFilterRespuestaAusente() {
        CorrectAnswerValidationFilter filter = new CorrectAnswerValidationFilter();
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "MULTIPLE_CHOICE", "Arquitectura de software",
                OPCIONES_VALIDAS, "Respuesta que no existe en las opciones");
        assertFalse(filter.process(request));
    }

    /**
     * @brief Una solicitud válida pasa los cuatro filtros.
     */
    @Test
    public void testPipelineCompletoConSolicitudValida() {
        QuestionPipeline pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());

        assertTrue(pipeline.execute(requestValida()));
    }

    /**
     * @brief Con el título vacío, el pipeline se detiene en el primer filtro.
     */
    @Test
    public void testPipelineCompletoSeDetieneEnElPrimerFiltroQueFalla() {
        QuestionPipeline pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());

        QuestionRequest requestInvalida = new QuestionRequest(
                "", "Contenido", "MULTIPLE_CHOICE", "Arquitectura de software",
                OPCIONES_VALIDAS, "Single Responsibility");

        assertFalse(pipeline.execute(requestInvalida));
    }
}
