/**
 * @file QuestionPipeline.java
 * @brief Tubería de filtros de validación.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.pipeline.base;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief Ejecuta los filtros en orden y se detiene en el primero que falla (fail-fast).
 *
 * Informa en la consola qué filtro rechazó la solicitud.
 */
public class QuestionPipeline {

    /** Filtros en el orden en que se ejecutan. */
    private List<QuestionFilter> filters = new ArrayList<>();

    /**
     * @brief Agrega un filtro al final de la tubería.
     * @param filter Filtro a agregar
     */
    public void addFilter(QuestionFilter filter) {
        filters.add(filter);
    }

    /**
     * @brief Pasa la solicitud por todos los filtros.
     * @param request Solicitud a validar
     * @return true si pasa todos los filtros
     */
    public boolean execute(QuestionRequest request) {
        for (QuestionFilter filter : filters) {
            if (!filter.process(request)) {
                System.err.println("La validación falló en el filtro: "
                        + filter.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }
}
