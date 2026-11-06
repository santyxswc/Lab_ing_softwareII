/**
 * @file ValidationResponse.java
 * @brief Cuerpo JSON devuelto por POST /validate.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.validation.infrastructure.rest;

import co.edu.unicauca.saberpro.validation.domain.ValidationResult;

/**
 * @brief Respuesta de validación.
 *
 * @param valid   {@code true} si la justificación es aceptable.
 * @param length  Caracteres evaluados.
 * @param message Explicación del resultado.
 */
public record ValidationResponse(boolean valid, int length, String message) {

    /**
     * @brief Convierte el resultado de dominio a DTO.
     * @param result Resultado de la regla.
     * @return DTO listo para serializar.
     */
    public static ValidationResponse from(ValidationResult result) {
        return new ValidationResponse(result.valid(), result.length(), result.message());
    }
}
