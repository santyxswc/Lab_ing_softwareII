/**
 * @file ValidationRequest.java
 * @brief Cuerpo JSON recibido en POST /validate.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.validation.infrastructure.rest;

/**
 * @brief Petición de validación.
 *
 * @param justification Justificación del reactivo a validar.
 */
public record ValidationRequest(String justification) {
}
