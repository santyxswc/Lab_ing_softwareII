/**
 * @file QuestionMicrokernel.java
 * @brief Núcleo del patrón Microkernel.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @brief Núcleo que delega cada solicitud en el plugin que la soporta y administra el banco de preguntas.
 *
 * El núcleo depende solo de la interfaz QuestionPlugin (inversión de dependencias): recibe los plugins
 * por constructor, así que las pruebas pueden inyectar plugins propios. El constructor sin argumentos
 * los descubre con PluginLoader a partir de plugins.properties; agregar un tipo de pregunta solo requiere
 * una clase nueva y una línea en ese archivo, sin modificar el núcleo (abierto/cerrado).
 */
public class QuestionMicrokernel {

    private static final Logger LOG = Logger.getLogger(QuestionMicrokernel.class.getName());

    /** Banco de preguntas, por id, en orden de creación. */
    private final Map<String, Question> questions = new LinkedHashMap<>();
    /** Plugins disponibles. */
    private final List<QuestionPlugin> plugins;

    /**
     * @brief Crea el núcleo con los plugins declarados en plugins.properties.
     * @throws IllegalStateException Si el registro no existe o un plugin no se puede cargar
     */
    public QuestionMicrokernel() {
        this(PluginLoader.desdeClasspath(PluginLoader.REGISTRO_POR_DEFECTO));
    }

    /**
     * @brief Crea el núcleo con un conjunto explícito de plugins.
     * @param plugins Plugins disponibles
     */
    public QuestionMicrokernel(List<QuestionPlugin> plugins) {
        this.plugins = List.copyOf(Objects.requireNonNull(plugins, "plugins"));
    }

    /**
     * @brief Genera una pregunta con el plugin que soporta el tipo y la agrega al banco.
     * @param type Tipo de pregunta (por ejemplo "MULTIPLE_CHOICE")
     * @param request Datos de la pregunta
     * @return La pregunta agregada, o vacío si el plugin rechazó la solicitud en sus validaciones
     * @throws IllegalArgumentException Si ningún plugin soporta el tipo
     */
    public Optional<Question> executePlugin(String type, QuestionRequest request) {
        QuestionPlugin plugin = plugins.stream()
                .filter(p -> p.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay ningún plugin registrado que soporte el tipo: " + type));

        Question question = plugin.generate(request);
        if (question == null) {
            LOG.warning(() -> "El plugin " + plugin.getName() + " rechazó la solicitud (no superó sus validaciones).");
            return Optional.empty();
        }

        questions.put(question.getId(), question);
        LOG.info(() -> "Pregunta agregada al banco con ID: " + question.getId());
        return Optional.of(question);
    }

    /**
     * @brief Preguntas del banco.
     * @return Vista de solo lectura, por id
     */
    public Map<String, Question> getQuestions() {
        return Collections.unmodifiableMap(questions);
    }

    /**
     * @brief Plugins disponibles.
     * @return Lista inmutable
     */
    public List<QuestionPlugin> getPlugins() {
        return plugins;
    }
}
