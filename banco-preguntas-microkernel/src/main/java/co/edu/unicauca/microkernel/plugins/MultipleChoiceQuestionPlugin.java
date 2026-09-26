/**
 * @file MultipleChoiceQuestionPlugin.java
 * @brief Plugin de preguntas de selección múltiple.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import co.edu.unicauca.microkernel.pipeline.base.QuestionPipeline;
import co.edu.unicauca.microkernel.pipeline.filters.*;

import java.util.UUID;

/**
 * @brief Genera preguntas de selección múltiple validadas con el pipeline completo.
 *
 * Filtros, en orden: ContentValidationFilter, OptionsValidationFilter, ClassificationFilter y
 * CorrectAnswerValidationFilter.
 */
public class MultipleChoiceQuestionPlugin implements QuestionPlugin {

    /** Pipeline con los cuatro filtros. */
    private QuestionPipeline pipeline;

    /**
     * @brief Crea el plugin y arma su pipeline de validación.
     */
    public MultipleChoiceQuestionPlugin() {
        pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());
    }

    /**
     * @brief Nombre del plugin.
     * @return "multiple-choice"
     */
    @Override
    public String getName() {
        return "multiple-choice";
    }

    /**
     * @brief Indica si el tipo es MULTIPLE_CHOICE.
     * @param type Tipo de pregunta
     * @return true para MULTIPLE_CHOICE
     */
    @Override
    public boolean supports(String type) {
        return "MULTIPLE_CHOICE".equalsIgnoreCase(type);
    }

    /**
     * @brief Valida la solicitud y genera la pregunta con un id UUID.
     * @param request Datos de la solicitud
     * @return La pregunta, o null si no pasa las validaciones
     */
    @Override
    public Question generate(QuestionRequest request) {
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
