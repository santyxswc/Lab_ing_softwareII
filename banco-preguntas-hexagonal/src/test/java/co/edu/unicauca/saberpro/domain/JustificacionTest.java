/**
 * @file JustificacionTest.java
 * @brief Pruebas unitarias para el Objeto de Valor Justificacion.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.domain.model.Justificacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dominio :: Objeto de Valor - Justificacion")
class JustificacionTest {

    @Test
    @DisplayName("Debe crear una justificación válida con más de 15 caracteres")
    void debeCrearJustificacionValida() {
        String texto = "Esta es una justificación técnica suficientemente detallada para el banco.";
        Justificacion justificacion = new Justificacion(texto);
        assertEquals(texto, justificacion.valor());
        assertEquals(texto, justificacion.getValor());
    }

    @Test
    @DisplayName("Debe aceptar una justificación con exactamente 15 caracteres (límite inferior)")
    void debeAceptarExactamente15Caracteres() {
        String texto15 = "123456789012345"; // 15 caracteres
        assertEquals(15, texto15.length());
        assertDoesNotThrow(() -> new Justificacion(texto15));
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException con 14 caracteres (justo debajo del umbral)")
    void debeFallarCon14Caracteres() {
        String texto14 = "12345678901234"; // 14 caracteres
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> new Justificacion(texto14));
        assertTrue(ex.getMessage().contains("al menos 15 caracteres"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Corta", "Texto breve", "   12345678901234   "})
    @DisplayName("Debe lanzar ReglaNegocioException si es nula, vacía o menor a 15 caracteres tras trim()")
    void debeLanzarExcepcionConTextoInsuficiente(String invalido) {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion(invalido));
    }
}

