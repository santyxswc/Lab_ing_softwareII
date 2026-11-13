/**
 * @file Justificacion.java
 * @brief Objeto de Valor inmutable para la justificación técnica de la pregunta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

/**
 * Objeto de Valor inmutable que representa la justificación de una pregunta.
 * Exige un mínimo de 15 caracteres de sustentación técnica. Si no cumple,
 * lanza {@link ReglaNegocioException}.
 */
public record Justificacion(String valor) {

    public static final int LONGITUD_MINIMA = 15;

    public Justificacion {
        if (valor == null || valor.trim().length() < LONGITUD_MINIMA) {
            throw new ReglaNegocioException(
                "La justificación debe poseer al menos 15 caracteres de sustentación técnica."
            );
        }
        valor = valor.trim();
    }

    public String getValor() {
        return valor;
    }
}

