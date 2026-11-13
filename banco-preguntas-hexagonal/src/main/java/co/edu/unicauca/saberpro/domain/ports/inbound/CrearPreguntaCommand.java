/**
 * @file CrearPreguntaCommand.java
 * @brief Comando de entrada para el caso de uso de creación de una pregunta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.ports.inbound;

import java.util.List;

/**
 * DTO inmutable de entrada que transporta los datos primitivos requeridos
 * para crear un nuevo reactivo y enviarlo a revisión.
 */
public record CrearPreguntaCommand(
    String enunciado,
    String justificacion,
    List<OpcionItem> opciones
) {
    public record OpcionItem(String texto, boolean esCorrecta) {}
}

