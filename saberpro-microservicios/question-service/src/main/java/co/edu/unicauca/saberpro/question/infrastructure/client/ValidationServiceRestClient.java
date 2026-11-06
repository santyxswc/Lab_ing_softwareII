/**
 * @file ValidationServiceRestClient.java
 * @brief Cliente REST síncrono hacia Validation Service (POST /validate).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.client;

import co.edu.unicauca.saberpro.question.application.exception.ValidationServiceUnavailableException;
import co.edu.unicauca.saberpro.question.application.port.JustificationValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @brief Implementa {@link JustificationValidator} con RestTemplate (Punto 2.1).
 *
 * La llamada es bloqueante: Question Service espera la respuesta antes de
 * decidir si guarda la pregunta.
 */
@Component
public class ValidationServiceRestClient implements JustificationValidator {

    /** Cuerpo enviado a Validation Service. */
    record ValidateRequest(String justification) {
    }

    /** Cuerpo recibido de Validation Service. */
    record ValidateResponse(boolean valid, int length, String message) {
    }

    private final RestTemplate restTemplate;
    private final String validateUrl;

    /**
     * @brief Construye el cliente.
     * @param restTemplate Cliente HTTP configurado en {@code RestClientConfig}.
     * @param baseUrl      URL base de Validation Service (propiedad
     *                     {@code services.validation.url}).
     */
    public ValidationServiceRestClient(RestTemplate restTemplate,
                                       @Value("${services.validation.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.validateUrl = baseUrl + "/validate";
    }

    /**
     * @brief Llama a POST /validate.
     * @param justification Texto a validar.
     * @return Veredicto del servicio remoto.
     * @throws ValidationServiceUnavailableException si hay error de red, error
     *         HTTP o respuesta vacía.
     */
    @Override
    public Outcome validate(String justification) {
        try {
            ValidateResponse response = restTemplate.postForObject(
                    validateUrl, new ValidateRequest(justification), ValidateResponse.class);
            return Optional.ofNullable(response)
                    .map(r -> new Outcome(r.valid(), r.message()))
                    .orElseThrow(() -> new ValidationServiceUnavailableException(
                            "Validation Service devolvió una respuesta vacía.", null));
        } catch (RestClientException e) {
            throw new ValidationServiceUnavailableException(
                    "No fue posible contactar a Validation Service en " + validateUrl + ".", e);
        }
    }
}
