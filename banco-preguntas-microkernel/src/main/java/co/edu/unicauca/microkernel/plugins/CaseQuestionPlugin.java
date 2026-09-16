package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import co.edu.unicauca.microkernel.pipeline.filters.ClassificationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;

import java.util.UUID;

/**
 * Plugin que genera preguntas de análisis de caso (un escenario extenso
 * que el estudiante debe interpretar). A diferencia de
 * MultipleChoiceQuestionPlugin, este tipo de pregunta no exige un
 * número fijo de opciones, así que reutiliza directamente dos filtros
 * de forma independiente (ContentValidationFilter y
 * ClassificationFilter) en lugar de ensamblar el QuestionPipeline
 * completo. Esto demuestra que los filtros son piezas reutilizables por
 * separado, no solo dentro de una única tubería fija.
 */
public class CaseQuestionPlugin implements QuestionPlugin {

    private final ContentValidationFilter contentValidationFilter = new ContentValidationFilter();
    private final ClassificationFilter classificationFilter = new ClassificationFilter();

    @Override
    public String getName() {
        return "case-question";
    }

    @Override
    public boolean supports(String type) {
        return "CASE_STUDY".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        if (!contentValidationFilter.process(request)) {
            return null;
        }
        if (!classificationFilter.process(request)) {
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
