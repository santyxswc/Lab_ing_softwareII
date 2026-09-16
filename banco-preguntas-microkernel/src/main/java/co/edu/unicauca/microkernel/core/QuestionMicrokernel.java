package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;

import java.io.InputStream;
import java.util.*;

/**
 * Núcleo central del patrón Microkernel. Administra el banco de
 * preguntas (Map&lt;String, Question&gt;) y carga dinámicamente los
 * plugins registrados en plugins.properties usando Reflexión, sin
 * conocer en tiempo de compilación ninguna clase concreta de plugin
 * (Principio de Inversión de Dependencias aplicado a nivel de carga
 * dinámica).
 *
 * Este mecanismo replica el visto en clase con
 * DeliveryPluginManager/plugin.properties para el envío de paquetes a
 * distintos países: un Properties mapea claves lógicas a nombres
 * completamente calificados de clase, y Class.forName +
 * getDeclaredConstructor().newInstance() instancia cada plugin sin
 * que el núcleo dependa de sus implementaciones concretas.
 */
public class QuestionMicrokernel {

    private Map<String, Question> questions = new HashMap<>();
    private List<QuestionPlugin> plugins = new ArrayList<>();

    public QuestionMicrokernel() {
        loadPlugins();
    }

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

                // Uso obligatorio de Reflexión para instanciación dinámica
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
     * Busca, entre los plugins cargados, el primero que soporte el tipo
     * indicado, y le delega la generación de la pregunta. Si la
     * pregunta se genera exitosamente (pasó sus validaciones internas),
     * se agrega al banco de preguntas.
     *
     * @param type    tipo de pregunta a generar
     * @param request datos de la solicitud
     * @throws IllegalArgumentException si ningún plugin registrado
     *                                  soporta el tipo indicado
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

    public Map<String, Question> getQuestions() {
        return questions;
    }

    public List<QuestionPlugin> getPlugins() {
        return plugins;
    }
}
