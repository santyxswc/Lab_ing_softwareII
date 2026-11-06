/**
 * @file ValidationResult.java
 * @brief Resultado inmutable de aplicar la regla de justificación.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.validation.domain;

/**
 * @brief Veredicto de la validación.
 *
 * @param valid   {@code true} si la justificación cumple la regla.
 * @param length  Número de caracteres evaluados (sin espacios en los extremos).
 * @param message Explicación legible del resultado.
 */
public record ValidationResult(boolean valid, int length, String message) {
}
