/**
 * @file Enunciado.java
 * @brief Objeto de Valor inmutable para el texto del enunciado.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.domain.model;

import co.edu.unicauca.saberpro.question.domain.exception.ReglaNegocioException;

/**
 * @brief Objeto de Valor inmutable que representa el enunciado de un reactivo.
 *
 * Garantiza que el texto no sea nulo ni vacío. Es Java puro: no depende de
 * Spring ni de JPA.
 *
 * @param valor Texto del enunciado.
 */
public record Enunciado(String valor) {

    /**
     * @brief Valida la invariante del Objeto de Valor.
     * @throws ReglaNegocioException si el enunciado es nulo o vacío.
     */
    public Enunciado {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ReglaNegocioException("El enunciado no puede ser nulo ni vacío.");
        }
        valor = valor.trim();
    }

    /** @return Texto del enunciado. */
    public String getValor() {
        return valor;
    }
}
