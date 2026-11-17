/**
 * @file CrearPreguntaUseCase.java
 * @brief Puerto de Entrada (Driving / Inbound) para crear reactivos.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.ports.inbound;

import co.edu.unicauca.saberpro.question.domain.model.Question;

/**
 * @brief Puerto de Entrada de la Arquitectura Hexagonal.
 *
 * Define la operación de creación de un reactivo. La implementa un servicio
 * de aplicación ({@code QuestionService}) y la invoca el adaptador REST.
 */
public interface CrearPreguntaUseCase {

    /**
     * @brief Crea un reactivo validando su estructura y su justificación.
     * @param command Datos del reactivo y sus opciones.
     * @return El agregado {@link Question} persistido en estado DRAFT.
     */
    Question crearPregunta(CrearPreguntaCommand command);
}
