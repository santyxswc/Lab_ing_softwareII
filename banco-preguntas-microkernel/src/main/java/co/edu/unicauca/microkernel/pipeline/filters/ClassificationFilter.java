/**
 * @file ClassificationFilter.java
 * @brief Filtro de área de conocimiento.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

import java.util.Set;

/**
 * @brief Valida que el área de conocimiento sea una de las áreas Saber Pro de Ingeniería de Sistemas.
 */
public class ClassificationFilter implements QuestionFilter {

    /** Áreas de conocimiento válidas. */
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
     * @brief Áreas válidas, para que la interfaz las muestre sin duplicar la lista.
     * @return Conjunto de áreas de conocimiento
     */
    public static Set<String> getValidClassifications() {
        return VALID_CLASSIFICATIONS;
    }

    /**
     * @brief Valida el área de conocimiento sin distinguir mayúsculas.
     * @param request Solicitud a validar
     * @return true si el área está en la lista
     */
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
