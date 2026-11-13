/**
 * @file OpcionRespuestaTest.java
 * @brief Pruebas unitarias para el Objeto de Valor OpcionRespuesta.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.domain.model.OpcionRespuesta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dominio :: Objeto de Valor - OpcionRespuesta")
class OpcionRespuestaTest {

    @Test
    @DisplayName("Debe crear una opción de respuesta correctamente como clave verdadera")
    void debeCrearOpcionCorrecta() {
        OpcionRespuesta opcion = new OpcionRespuesta("Arquitectura Hexagonal", true);
        assertEquals("Arquitectura Hexagonal", opcion.texto());
        assertEquals("Arquitectura Hexagonal", opcion.getTexto());
        assertTrue(opcion.esCorrecta());
        assertTrue(opcion.isEsCorrecta());
    }

    @Test
    @DisplayName("Debe crear una opción distractora (clave falsa)")
    void debeCrearOpcionDistractora() {
        OpcionRespuesta opcion = new OpcionRespuesta("Monolito sin capas", false);
        assertEquals("Monolito sin capas", opcion.texto());
        assertFalse(opcion.esCorrecta());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    @DisplayName("Debe lanzar ReglaNegocioException si el texto de la opción es nulo o vacío")
    void debeFallarSiTextoEsInvalido(String textoInvalido) {
        assertThrows(ReglaNegocioException.class, () -> new OpcionRespuesta(textoInvalido, false));
    }
}

