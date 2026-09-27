/**
 * @file PluginLoader.java
 * @brief Descubrimiento de plugins declarados en un archivo de propiedades.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @brief Instancia por reflexión los plugins registrados en un archivo .properties del classpath.
 *
 * Cada entrada asocia una clave con el nombre completo de una clase que implementa QuestionPlugin.
 * Se separa del núcleo para que QuestionMicrokernel solo administre plugins y preguntas: cómo se
 * descubren los plugins es una responsabilidad distinta (y reemplazable).
 */
public final class PluginLoader {

    /** Archivo de registro por defecto. */
    public static final String REGISTRO_POR_DEFECTO = "plugins.properties";

    private static final Logger LOG = Logger.getLogger(PluginLoader.class.getName());

    private PluginLoader() {
    }

    /**
     * @brief Carga los plugins declarados en un recurso del classpath.
     * @param recurso Nombre del archivo, por ejemplo "plugins.properties"
     * @return Plugins en el orden de sus claves
     * @throws IllegalStateException Si el archivo no existe o una clase no se puede instanciar
     */
    public static List<QuestionPlugin> desdeClasspath(String recurso) {
        Properties registro = new Properties();
        try (InputStream entrada = PluginLoader.class.getClassLoader().getResourceAsStream(recurso)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró el registro de plugins: " + recurso);
            }
            registro.load(entrada);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el registro de plugins: " + recurso, e);
        }

        List<QuestionPlugin> plugins = new ArrayList<>();
        // Properties no conserva el orden del archivo: se ordena por clave para cargar siempre igual.
        for (String clave : registro.stringPropertyNames().stream().sorted().toList()) {
            plugins.add(instanciar(registro.getProperty(clave).trim()));
        }
        return plugins;
    }

    /**
     * @brief Crea un plugin por su nombre de clase.
     * @param nombreClase Nombre completo de la clase
     * @return Instancia del plugin
     * @throws IllegalStateException Si la clase no existe, no implementa QuestionPlugin o no tiene constructor vacío
     */
    static QuestionPlugin instanciar(String nombreClase) {
        try {
            Class<?> clase = Class.forName(nombreClase);
            if (!QuestionPlugin.class.isAssignableFrom(clase)) {
                throw new IllegalStateException(nombreClase + " no implementa QuestionPlugin");
            }
            QuestionPlugin plugin = (QuestionPlugin) clase.getDeclaredConstructor().newInstance();
            LOG.info(() -> "Plugin cargado vía reflexión: " + plugin.getName() + " (" + nombreClase + ")");
            return plugin;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No se pudo instanciar el plugin " + nombreClase, e);
        }
    }
}
