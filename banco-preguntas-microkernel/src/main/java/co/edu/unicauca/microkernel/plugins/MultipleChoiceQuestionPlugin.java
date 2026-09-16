package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import co.edu.unicauca.microkernel.pipeline.base.QuestionPipeline;
import co.edu.unicauca.microkernel.pipeline.filters.*;

import java.util.UUID;

/**
 * Plugin que genera preguntas de selección múltiple.
 *
 * Es el plugin que integra OBLIGATORIAMENTE el pipeline completo de
 * validación (los 4 filtros): ContentValidationFilter,
 * OptionsValidationFilter, ClassificationFilter y
 * CorrectAnswerValidationFilter, en ese orden.
 */
public class MultipleChoiceQuestionPlugin implements QuestionPlugin {

    private QuestionPipeline pipeline;

    public MultipleChoiceQuestionPlugin() {
        pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());
    }

    @Override
    public String getName() {
        return "multiple-choice";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIPLE_CHOICE".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {
        // Ejecución del pipeline de validación previo a la generación
        if (!pipeline.execute(request)) {
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
