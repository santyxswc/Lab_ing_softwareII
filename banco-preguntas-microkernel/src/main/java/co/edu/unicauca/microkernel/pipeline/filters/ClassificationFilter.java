package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

import java.util.Set;

/**
 * Valida que el área de conocimiento (clasificación) de la pregunta
 * pertenezca al conjunto de áreas reconocidas por el Banco de Preguntas
 * Saber Pro del programa de Ingeniería de Sistemas.
 */
public class ClassificationFilter implements QuestionFilter {

    private static final Set<String> VALID_CLASSIFICATIONS = Set.of(
            "Arquitectura de software",
            "Ingeniería de software",
            "Estructuras de datos",
            "Bases de datos",
            "Redes de computadores",
            "Programación orientada a objetos",
            "Matemáticas discretas",
            "Sistemas operativos"
    );

    /**
     * Expone la lista de áreas de conocimiento reconocidas, para que
     * la capa de presentación (Main / Swing) pueda ofrecerlas, por
     * ejemplo, en un combo, sin duplicar esta lista.
     */
    public static Set<String> getValidClassifications() {
        return VALID_CLASSIFICATIONS;
    }

    @Override
    public boolean process(QuestionRequest request) {
        if (request.getClassification() == null || request.getClassification().trim().isEmpty()) {
            return false;
        }
        String normalized = request.getClassification().trim();
        return VALID_CLASSIFICATIONS.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(normalized));
    }
}
