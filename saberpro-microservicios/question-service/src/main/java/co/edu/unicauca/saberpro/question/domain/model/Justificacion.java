/**
 * @file Justificacion.java
 * @brief Objeto de Valor inmutable para la justificación técnica del reactivo.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.model;

import co.edu.unicauca.saberpro.question.domain.exception.ReglaNegocioException;

/**
 * @brief Objeto de Valor inmutable que representa la justificación de la
 *        respuesta correcta.
 *
 * Exige un mínimo de {@value #LONGITUD_MINIMA} caracteres de sustentación
 * técnica; si no se cumple lanza {@link ReglaNegocioException}. Es Java puro.
 *
 * @param valor Texto de la justificación.
 */
public record Justificacion(String valor) {

    /** Longitud mínima exigida (inclusive). */
    public static final int LONGITUD_MINIMA = 15;

    /**
     * @brief Valida la invariante del Objeto de Valor.
     * @throws ReglaNegocioException si la justificación es nula o no alcanza
     *         la longitud mínima.
     */
    public Justificacion {
        if (valor == null || valor.trim().length() < LONGITUD_MINIMA) {
            throw new ReglaNegocioException(
                    "La justificación debe poseer al menos " + LONGITUD_MINIMA
                            + " caracteres de sustentación técnica.");
        }
        valor = valor.trim();
    }

    /** @return Texto de la justificación. */
    public String getValor() {
        return valor;
    }
}
