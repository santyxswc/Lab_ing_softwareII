package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del núcleo QuestionMicrokernel.
 *
 * Estas pruebas ejercitan la carga REAL de plugins vía Reflexión desde
 * plugins.properties (tal como quedará configurado en producción), ya
 * que QuestionMicrokernel no recibe los plugins por inyección sino que
 * los descubre él mismo — es precisamente el comportamiento que el
 * patrón Microkernel busca demostrar.
 */
public class QuestionMicrokernelTest {

    private QuestionMicrokernel microkernel;

    private static final List<String> OPCIONES_VALIDAS = Arrays.asList(
            "Single Responsibility", "Open Closed", "Liskov", "Interface Segregation");

    @BeforeEach
    void setUp() {
        microkernel = new QuestionMicrokernel();
    }

    @Test
    void cargaLosTresPluginsDeclaradosEnPluginsProperties() {
        assertEquals(3, microkernel.getPlugins().size());
    }

    @Test
    void elBancoDePreguntasEmpiezaVacio() {
        assertTrue(microkernel.getQuestions().isEmpty());
    }

    @Test
    void generaYAlmacenaUnaPreguntaDeSeleccionMultipleValida() {
        QuestionRequest request = new QuestionRequest(
                "Pregunta SOLID",
                "¿Qué representa la S en SOLID?",
                "MULTIPLE_CHOICE",
                "Arquitectura de software",
                OPCIONES_VALIDAS,
                "Single Responsibility"
        );

        microkernel.executePlugin("MULTIPLE_CHOICE", request);

        assertEquals(1, microkernel.getQuestions().size());
    }

    @Test
    void noAlmacenaUnaPreguntaDeSeleccionMultipleQueNoPasaElPipeline() {
        // Sin las 4 opciones requeridas: debe fallar en OptionsValidationFilter.
        QuestionRequest request = new QuestionRequest(
                "Pregunta SOLID",
                "¿Qué representa la S en SOLID?",
                "MULTIPLE_CHOICE",
                "Arquitectura de software",
                Arrays.asList("Single Responsibility", "Open Closed"),
                "Single Responsibility"
        );

        microkernel.executePlugin("MULTIPLE_CHOICE", request);

        assertTrue(microkernel.getQuestions().isEmpty());
    }

    @Test
    void generaUnaPreguntaDeCaso() {
        QuestionRequest request = new QuestionRequest(
                "Caso: migración de monolito",
                "Una empresa necesita migrar su sistema monolítico a microservicios...",
                "CASE_STUDY",
                "Arquitectura de software",
                null,
                null
        );

        microkernel.executePlugin("CASE_STUDY", request);

        assertEquals(1, microkernel.getQuestions().size());
    }

    @Test
    void generaUnaPreguntaMultimedia() {
        QuestionRequest request = new QuestionRequest(
                "Diagrama de arquitectura",
                "Video: https://ejemplo.edu.co/video-arquitectura.mp4 — Analice el diagrama presentado.",
                "MULTIMEDIA",
                "Arquitectura de software",
                null,
                null
        );

        microkernel.executePlugin("MULTIMEDIA", request);

        assertEquals(1, microkernel.getQuestions().size());
    }

    @Test
    void lanzaExcepcionSiNingunPluginSoportaElTipoSolicitado() {
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "TIPO_INEXISTENTE",
                "Arquitectura de software", OPCIONES_VALIDAS, "Single Responsibility");

        assertThrows(IllegalArgumentException.class,
                () -> microkernel.executePlugin("TIPO_INEXISTENTE", request));
    }
}
