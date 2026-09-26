/**
 * @file QuestionPlugin.java
 * @brief Contrato de los plugins de preguntas.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.common.interfaces;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

/**
 * @brief Contrato que cumplen todos los plugins que generan preguntas.
 *
 * El núcleo solo depende de esta abstracción, nunca de las implementaciones concretas
 * (principio de inversión de dependencias).
 */
public interface QuestionPlugin {

    /**
     * @brief Nombre del plugin, para los mensajes de la consola.
     * @return Nombre del plugin
     */
    String getName();

    /**
     * @brief Indica si el plugin genera preguntas de un tipo.
     * @param type Tipo de pregunta
     * @return true si lo soporta
     */
    boolean supports(String type);

    /**
     * @brief Genera una pregunta a partir de la solicitud.
     * @param request Datos de la solicitud
     * @return La pregunta generada, o null si la solicitud no pasa las validaciones del plugin
     */
    Question generate(QuestionRequest request);
}
