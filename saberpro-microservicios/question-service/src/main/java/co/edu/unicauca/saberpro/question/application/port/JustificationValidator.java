/**
 * @file JustificationValidator.java
 * @brief Puerto de salida hacia Validation Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.application.port;

/**
 * @brief Valida la justificación de un reactivo en un servicio externo.
 *
 * La implementación ({@code ValidationServiceRestClient}) usa RestTemplate
 * de forma síncrona.
 */
public interface JustificationValidator {

    /**
     * @brief Resultado de la validación remota.
     *
     * @param valid   {@code true} si la justificación es aceptable.
     * @param message Explicación del resultado.
     */
    record Outcome(boolean valid, String message) {
    }

    /**
     * @brief Valida la justificación.
     * @param justification Texto a validar.
     * @return Veredicto del servicio.
     * @throws co.edu.unicauca.saberpro.question.application.exception.ValidationServiceUnavailableException
     *         si el servicio no responde.
     */
    Outcome validate(String justification);
}
