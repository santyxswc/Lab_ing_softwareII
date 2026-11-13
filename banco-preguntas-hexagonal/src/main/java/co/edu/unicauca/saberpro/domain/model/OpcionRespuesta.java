/**
 * @file OpcionRespuesta.java
 * @brief Objeto de Valor inmutable para una opción de respuesta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

/**
 * Objeto de Valor inmutable que representa una opción de respuesta.
 * Contiene el texto descriptivo y un booleano indicando si es la clave correcta.
 */
public record OpcionRespuesta(String texto, boolean esCorrecta) {

    public OpcionRespuesta {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ReglaNegocioException("El texto de la opción de respuesta no puede ser nulo ni vacío.");
        }
        texto = texto.trim();
    }

    public String getTexto() {
        return texto;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }
}

