/**
 * @file QuestionMicrokernelTest.java
 * @brief Pruebas del núcleo QuestionMicrokernel.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @brief Pruebas del núcleo con la carga real de plugins desde plugins.properties.
 *
 * El núcleo descubre los plugins por reflexión en lugar de recibirlos inyectados, así que las pruebas usan
 * la misma configuración que la aplicación.
 */
public class QuestionMicrokernelTest {

    /** Núcleo bajo prueba, nuevo en cada prueba. */
    private QuestionMicrokernel microkernel;

    /** Cuatro opciones válidas de ejemplo. */
    private static final List<String> OPCIONES_VALIDAS = Arrays.asList(
            "Single Responsibility", "Open Closed", "Liskov", "Interface Segregation");

    /**
     * @brief Crea un núcleo nuevo antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        microkernel = new QuestionMicrokernel();
    }

    /**
     * @brief Se cargan los tres plugins declarados.
     */
    @Test
    void cargaLosTresPluginsDeclaradosEnPluginsProperties() {
        assertEquals(3, microkernel.getPlugins().size());
    }

    /**
     * @brief El banco empieza sin preguntas.
     */
    @Test
    void elBancoDePreguntasEmpiezaVacio() {
        assertTrue(microkernel.getQuestions().isEmpty());
    }

    /**
     * @brief Una pregunta de selección múltiple válida se agrega al banco.
     */
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

    /**
     * @brief Con solo dos opciones, OptionsValidationFilter la rechaza y no se agrega.
     */
    @Test
    void noAlmacenaUnaPreguntaDeSeleccionMultipleQueNoPasaElPipeline() {
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

    /**
     * @brief Una pregunta de caso válida se agrega al banco.
     */
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

    /**
     * @brief Una pregunta multimedia válida se agrega al banco.
     */
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

    /**
     * @brief Un tipo sin plugin lanza IllegalArgumentException.
     */
    @Test
    void lanzaExcepcionSiNingunPluginSoportaElTipoSolicitado() {
        QuestionRequest request = new QuestionRequest(
                "Pregunta", "Contenido", "TIPO_INEXISTENTE",
                "Arquitectura de software", OPCIONES_VALIDAS, "Single Responsibility");

        assertThrows(IllegalArgumentException.class,
                () -> microkernel.executePlugin("TIPO_INEXISTENTE", request));
    }
}
