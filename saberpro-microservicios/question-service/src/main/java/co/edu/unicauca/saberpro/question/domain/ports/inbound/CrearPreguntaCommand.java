/**
 * @file CrearPreguntaCommand.java
 * @brief Comando de entrada del caso de uso "crear pregunta".
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.ports.inbound;

import java.util.List;

/**
 * @brief Comando para crear un reactivo.
 *
 * DTO inmutable de entrada que transporta los datos primitivos del reactivo.
 * Vive en el dominio (puertos de entrada) para que el puerto
 * {@link CrearPreguntaUseCase} no dependa de capas externas.
 *
 * @param statement     Enunciado.
 * @param justification Justificación de la respuesta correcta.
 * @param options       Opciones de respuesta.
 * @param correctAnswer Letra de la opción correcta.
 */
public record CrearPreguntaCommand(String statement, String justification,
                                   List<OptionData> options, String correctAnswer) {

    /**
     * @brief Una opción tal como llega del cliente.
     *
     * @param label Letra.
     * @param text  Texto.
     */
    public record OptionData(String label, String text) {
    }
}
