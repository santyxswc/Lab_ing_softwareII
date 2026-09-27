/**
 * @file PluginInjectionTest.java
 * @brief Pruebas del núcleo con plugins inyectados y del cargador de plugins.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @brief Verifica que el núcleo solo depende de la interfaz QuestionPlugin.
 */
class PluginInjectionTest {

    /** Plugin de prueba: acepta un tipo y genera (o rechaza) sin validar nada. */
    private record PluginFalso(String tipo, boolean acepta) implements QuestionPlugin {
        @Override
        public String getName() {
            return "Falso " + tipo;
        }

        @Override
        public boolean supports(String type) {
            return tipo.equals(type);
        }

        @Override
        public Question generate(QuestionRequest request) {
            return acepta ? new Question(java.util.UUID.randomUUID().toString(), request.getTitle(), request.getContent(), tipo) : null;
        }
    }

    private static QuestionRequest solicitud(String tipo) {
        return new QuestionRequest("Título", "Contenido", tipo, "Matemáticas", null, null);
    }

    @Test
    void delegaEnElPluginQueSoportaElTipoSinUsarReflexion() {
        var kernel = new QuestionMicrokernel(List.of(new PluginFalso("A", true), new PluginFalso("B", true)));

        Optional<Question> pregunta = kernel.executePlugin("B", solicitud("B"));

        assertTrue(pregunta.isPresent());
        assertEquals("B", pregunta.get().getType());
        assertEquals(1, kernel.getQuestions().size());
    }

    @Test
    void unaSolicitudRechazadaDevuelveVacioYNoSeAgrega() {
        var kernel = new QuestionMicrokernel(List.of(new PluginFalso("A", false)));

        assertTrue(kernel.executePlugin("A", solicitud("A")).isEmpty());
        assertTrue(kernel.getQuestions().isEmpty());
    }

    @Test
    void elBancoYLosPluginsSonDeSoloLectura() {
        var kernel = new QuestionMicrokernel(List.of(new PluginFalso("A", true)));
        kernel.executePlugin("A", solicitud("A"));

        assertThrows(UnsupportedOperationException.class, () -> kernel.getQuestions().clear());
        assertThrows(UnsupportedOperationException.class, () -> kernel.getPlugins().clear());
    }

    @Test
    void elCargadorRespetaElOrdenDelRegistro() {
        List<QuestionPlugin> plugins = PluginLoader.desdeClasspath(PluginLoader.REGISTRO_POR_DEFECTO);

        assertEquals(List.of("MultipleChoiceQuestionPlugin", "CaseQuestionPlugin", "MultimediaQuestionPlugin"),
                plugins.stream().map(p -> p.getClass().getSimpleName()).toList());
    }

    @Test
    void elCargadorFallaConUnMensajeClaro() {
        var sinRegistro = assertThrows(IllegalStateException.class, () -> PluginLoader.desdeClasspath("no-existe.properties"));
        assertTrue(sinRegistro.getMessage().contains("no-existe.properties"));

        var noEsPlugin = assertThrows(IllegalStateException.class, () -> PluginLoader.instanciar("java.lang.String"));
        assertTrue(noEsPlugin.getMessage().contains("no implementa QuestionPlugin"));

        assertThrows(IllegalStateException.class, () -> PluginLoader.instanciar("co.edu.inexistente.Plugin"));
    }
}
