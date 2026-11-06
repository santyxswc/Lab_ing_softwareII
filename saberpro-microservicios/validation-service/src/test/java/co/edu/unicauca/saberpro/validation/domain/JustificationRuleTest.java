/**
 * @file JustificationRuleTest.java
 * @brief Pruebas unitarias de la regla de justificación.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.validation.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @brief Verifica el límite "longitud > 15".
 */
class JustificationRuleTest {

    private final JustificationRule rule = new JustificationRule();

    /** @brief 16 caracteres pasa la regla. */
    @Test
    void acceptsMoreThan15Characters() {
        assertTrue(rule.evaluate("1234567890123456").valid());
    }

    /** @brief Exactamente 15 caracteres no pasa (debe ser estrictamente mayor). */
    @Test
    void rejectsExactly15Characters() {
        assertFalse(rule.evaluate("123456789012345").valid());
    }

    /** @brief Los espacios de los extremos no cuentan. */
    @Test
    void ignoresSurroundingSpaces() {
        assertFalse(rule.evaluate("      corta       ").valid());
    }

    /** @brief Un valor nulo se trata como vacío. */
    @Test
    void rejectsNull() {
        assertFalse(rule.evaluate(null).valid());
    }
}
