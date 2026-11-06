/**
 * @file JustificationRule.java
 * @brief Regla de negocio que decide si una justificación es aceptable.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.validation.domain;

/**
 * @brief Regla: la justificación debe tener más de 15 caracteres.
 *
 * Se cuentan los caracteres después de quitar los espacios de los extremos,
 * para que una cadena rellena de espacios no pase la validación.
 * Es Java puro: no depende de Spring.
 */
public final class JustificationRule {

    /** Longitud que hay que superar (estrictamente mayor). */
    public static final int MIN_EXCLUSIVE_LENGTH = 15;

    /**
     * @brief Evalúa la justificación.
     * @param justification Texto a evaluar; puede ser {@code null}.
     * @return Resultado con el veredicto y un mensaje para el usuario.
     */
    public ValidationResult evaluate(String justification) {
        int length = justification == null ? 0 : justification.strip().length();
        boolean valid = length > MIN_EXCLUSIVE_LENGTH;
        String message = valid
                ? "Justificación válida."
                : "La justificación debe tener más de " + MIN_EXCLUSIVE_LENGTH
                  + " caracteres (tiene " + length + ").";
        return new ValidationResult(valid, length, message);
    }
}
