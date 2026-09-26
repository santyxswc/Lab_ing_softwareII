/**
 * @file CaseQuestionPlugin.java
 * @brief Plugin de preguntas de análisis de caso.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import co.edu.unicauca.microkernel.pipeline.filters.ClassificationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;

import java.util.UUID;

/**
 * @brief Genera preguntas de análisis de caso (un escenario extenso que el estudiante interpreta).
 *
 * No exige opciones, así que en lugar del pipeline completo usa por separado ContentValidationFilter y
 * ClassificationFilter: los filtros se pueden reutilizar individualmente.
 */
public class CaseQuestionPlugin implements QuestionPlugin {

    /** Valida título y contenido. */
    private final ContentValidationFilter contentValidationFilter = new ContentValidationFilter();
    /** Valida el área de conocimiento. */
    private final ClassificationFilter classificationFilter = new ClassificationFilter();

    /**
     * @brief Nombre del plugin.
     * @return "case-question"
     */
    @Override
    public String getName() {
        return "case-question";
    }

    /**
     * @brief Indica si el tipo es CASE_STUDY.
     * @param type Tipo de pregunta
     * @return true para CASE_STUDY
     */
    @Override
    public boolean supports(String type) {
        return "CASE_STUDY".equalsIgnoreCase(type);
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
