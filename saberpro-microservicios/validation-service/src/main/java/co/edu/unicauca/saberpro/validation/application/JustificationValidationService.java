/**
 * @file JustificationValidationService.java
 * @brief Caso de uso "validar justificación".
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.validation.application;

import co.edu.unicauca.saberpro.validation.domain.JustificationRule;
import co.edu.unicauca.saberpro.validation.domain.ValidationResult;
import org.springframework.stereotype.Service;

/**
 * @brief Orquesta la regla de dominio {@link JustificationRule}.
 */
@Service
public class JustificationValidationService {

    private final JustificationRule rule = new JustificationRule();

    /**
     * @brief Valida la justificación de un reactivo.
     * @param justification Texto enviado por Question Service.
     * @return Veredicto de la validación.
     */
    public ValidationResult validate(String justification) {
        return rule.evaluate(justification);
    }
}
