/**
 * @file Enunciado.java
 * @brief Objeto de Valor inmutable para el texto del enunciado de la pregunta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

/**
 * Objeto de Valor inmutable que representa el enunciado de una pregunta del Banco Saber PRO.
 * Garantiza que no sea nulo ni vacío.
 */
public record Enunciado(String valor) {

    public Enunciado {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ReglaNegocioException("El enunciado no puede ser nulo ni vacío.");
        }
        valor = valor.trim();
    }

    public String getValor() {
        return valor;
    }
}

