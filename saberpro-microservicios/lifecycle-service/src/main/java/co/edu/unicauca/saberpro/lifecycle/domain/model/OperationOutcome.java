/**
 * @file OperationOutcome.java
 * @brief Resultado del procesamiento de un comando asíncrono.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.model;

/**
 * @brief Cómo terminó una operación.
 */
public enum OperationOutcome {
    /** La transición se aplicó. */
    APPLIED,
    /** La transición no era válida en el estado actual. */
    REJECTED
}
