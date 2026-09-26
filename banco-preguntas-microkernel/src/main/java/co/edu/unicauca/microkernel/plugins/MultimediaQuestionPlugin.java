/**
 * @file MultimediaQuestionPlugin.java
 * @brief Plugin de preguntas multimedia.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;

import java.util.UUID;

/**
 * @brief Genera preguntas multimedia: el contenido incluye la referencia a un recurso (imagen o video) y la pregunta.
 *
 * Solo exige título y contenido.
 */
public class MultimediaQuestionPlugin implements QuestionPlugin {

    /** Valida título y contenido. */
    private final ContentValidationFilter contentValidationFilter = new ContentValidationFilter();

    /**
     * @brief Nombre del plugin.
     * @return "multimedia-question"
     */
    @Override
    public String getName() {
        return "multimedia-question";
    }

    /**
     * @brief Indica si el tipo es MULTIMEDIA.
     * @param type Tipo de pregunta
     * @return true para MULTIMEDIA
     */
    @Override
    public boolean supports(String type) {
        return "MULTIMEDIA".equalsIgnoreCase(type);
    }

    /**
     * @brief Valida la solicitud y genera la pregunta con un id UUID.
     * @param request Datos de la solicitud
     * @return La pregunta, o null si no pasa las validaciones
     */
    @Override
    public Question generate(QuestionRequest request) {
        if (!contentValidationFilter.process(request)) {
            return null;
        }
        return new Question(
                UUID.randomUUID().toString(),
                request.getTitle(),
                request.getContent(),
                request.getType()
        );
    }
}
