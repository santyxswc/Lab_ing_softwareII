package co.edu.unicauca.microkernel.pipeline.base;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Tubería (Pipeline) que ejecuta una lista de filtros de validación en
 * orden secuencial. Se detiene en el primer filtro que falle
 * (fail-fast), reportando cuál filtro fue.
 */
public class QuestionPipeline {

    private List<QuestionFilter> filters = new ArrayList<>();

    public void addFilter(QuestionFilter filter) {
        filters.add(filter);
    }

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
