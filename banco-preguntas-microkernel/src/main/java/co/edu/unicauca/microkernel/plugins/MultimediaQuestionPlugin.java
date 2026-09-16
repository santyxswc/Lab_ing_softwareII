package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;

import java.util.UUID;

/**
 * Plugin que genera preguntas multimedia (el contenido referencia un
 * recurso audiovisual, ej. una URL de imagen o video, más una
 * pregunta asociada). Solo exige que el título y el contenido (que
 * aquí incluye la referencia al recurso) no estén vacíos.
 */
public class MultimediaQuestionPlugin implements QuestionPlugin {

    private final ContentValidationFilter contentValidationFilter = new ContentValidationFilter();

    @Override
    public String getName() {
        return "multimedia-question";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIMEDIA".equalsIgnoreCase(type);
    }

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
