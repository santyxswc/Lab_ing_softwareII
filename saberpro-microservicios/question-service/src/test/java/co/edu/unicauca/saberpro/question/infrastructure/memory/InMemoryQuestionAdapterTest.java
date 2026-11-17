/**
 * @file InMemoryQuestionAdapterTest.java
 * @brief Prueba del adaptador en memoria contra el contrato del puerto.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.memory;

import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.question.infrastructure.PreguntaRepositoryPortContract;

/**
 * @brief Ejecuta el contrato sin Spring ni base de datos.
 */
class InMemoryQuestionAdapterTest extends PreguntaRepositoryPortContract {

    private final InMemoryQuestionAdapter adapter = new InMemoryQuestionAdapter();

    @Override
    protected PreguntaRepositoryPort port() {
        return adapter;
    }
}
