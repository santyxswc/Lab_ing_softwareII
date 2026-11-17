/**
 * @file EvaluarPreguntaUseCase.java
 * @brief Puerto de Entrada (Driving / Inbound) para enviar reactivos a evaluación.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.ports.inbound;

/**
 * @brief Puerto de Entrada de la Arquitectura Hexagonal.
 *
 * Define la operación de enviar un reactivo a evaluación (revisión) de forma
 * asíncrona. El servicio de aplicación publica la solicitud a través del
 * puerto de salida {@code NotificacionPort} y retorna enseguida el id de
 * operación.
 */
public interface EvaluarPreguntaUseCase {

    /**
     * @brief Envía un reactivo a evaluación (revisión) de forma asíncrona.
     * @param questionId Reactivo a enviar.
     * @return El id de operación que permite rastrear la solicitud.
     */
    String enviarAEvaluacion(Long questionId);
}
