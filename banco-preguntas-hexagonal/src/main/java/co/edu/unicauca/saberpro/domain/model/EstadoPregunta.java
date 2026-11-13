/**
 * @file EstadoPregunta.java
 * @brief Ciclo de vida del reactivo en el Banco de Preguntas Saber PRO.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.model;

/**
 * Estados del ciclo de vida de una pregunta.
 * Inicia en {@link #BORRADOR} y transiciona a {@link #PENDIENTE_REVISION}
 * al validar las invariantes del Agregado.
 */
public enum EstadoPregunta {
    BORRADOR,
    PENDIENTE_REVISION
}

