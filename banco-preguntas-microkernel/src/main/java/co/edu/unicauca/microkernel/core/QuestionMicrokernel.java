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

import java.io.InputStream;
import java.util.*;

/**
 * @brief Núcleo que carga los plugins y administra el banco de preguntas.
 *
 * Lee plugins.properties, que asocia claves a nombres completos de clases, e instancia cada plugin con
 * reflexión (Class.forName y getDeclaredConstructor().newInstance()). Así el núcleo no depende en tiempo
 * de compilación de ninguna clase concreta: agregar un tipo de pregunta solo requiere una clase nueva
 * y una línea en plugins.properties.
 */
public class QuestionMicrokernel {

    /** Banco de preguntas, por id. */
    private Map<String, Question> questions = new HashMap<>();
    /** Plugins cargados. */
    private List<QuestionPlugin> plugins = new ArrayList<>();

    /**
     * @brief Crea el núcleo y carga los plugins.
     */
    public QuestionMicrokernel() {
        loadPlugins();
    }

    /**
     * @brief Instancia con reflexión cada plugin declarado en plugins.properties.
     *
     * Los errores de carga se informan en la consola y no detienen la aplicación.
     */
    private void loadPlugins() {
        try {
            Properties prop = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("plugins.properties");
            if (input == null) {
                System.err.println("No se encontró el archivo plugins.properties");
                return;
            }
            prop.load(input);

            for (String key : prop.stringPropertyNames()) {
                String className = prop.getProperty(key);

                Class<?> clazz = Class.forName(className);
                QuestionPlugin plugin = (QuestionPlugin) clazz.getDeclaredConstructor().newInstance();
                plugins.add(plugin);
                System.out.println("Plugin cargado vía reflexión: " + plugin.getName()
                        + " (" + className + ")");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar plugins vía reflexión: " + e.getMessage());
        }
    }

    /**
     * @brief Delega la generación al primer plugin que soporte el tipo y guarda la pregunta si es válida.
     * @param type Tipo de pregunta
     * @param request Datos de la solicitud
     * @throws IllegalArgumentException Si ningún plugin soporta el tipo
     */
    public void executePlugin(String type, QuestionRequest request) {
        for (QuestionPlugin plugin : plugins) {
            if (plugin.supports(type)) {
                Question question = plugin.generate(request);
                if (question != null) {
                    questions.put(question.getId(), question);
                    System.out.println("Pregunta agregada exitosamente al banco con ID: "
                            + question.getId());
                } else {
                    System.err.println("El plugin " + plugin.getName()
                            + " rechazó la solicitud (no superó sus validaciones).");
                }
                return;
            }
        }
        throw new IllegalArgumentException("No hay ningún plugin registrado que soporte el tipo: " + type);
    }

    /**
     * @brief Obtiene el banco de preguntas.
     * @return Preguntas por id
     */
    public Map<String, Question> getQuestions() {
        return questions;
    }

    /**
     * @brief Obtiene los plugins cargados.
     * @return Plugins
     */
    public List<QuestionPlugin> getPlugins() {
        return plugins;
    }
}
