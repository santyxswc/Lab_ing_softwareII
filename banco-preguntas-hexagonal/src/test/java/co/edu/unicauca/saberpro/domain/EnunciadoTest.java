/**
 * @file EnunciadoTest.java
 * @brief Pruebas unitarias para el Objeto de Valor Enunciado.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.domain.model.Enunciado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dominio :: Objeto de Valor - Enunciado")
class EnunciadoTest {

    @Test
    @DisplayName("Debe crear un enunciado válido y eliminar espacios en blanco sobrantes")
    void debeCrearEnunciadoValido() {
        Enunciado enunciado = new Enunciado("   ¿Cuál es el patrón que separa la lógica de dominio de la infraestructura?   ");
        assertEquals("¿Cuál es el patrón que separa la lógica de dominio de la infraestructura?", enunciado.valor());
        assertEquals("¿Cuál es el patrón que separa la lógica de dominio de la infraestructura?", enunciado.getValor());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Debe lanzar ReglaNegocioException si el enunciado es nulo, vacío o puros espacios")
    void debeLanzarExcepcionCuandoEnunciadoInvalido(String valorInvalido) {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> new Enunciado(valorInvalido));
        assertEquals("El enunciado no puede ser nulo ni vacío.", ex.getMessage());
    }

    @Test
    @DisplayName("Dos enunciados con el mismo texto deben ser iguales por valor (inmutabilidad)")
    void debeCumplirIgualdadPorValor() {
        Enunciado e1 = new Enunciado("Texto de prueba");
        Enunciado e2 = new Enunciado("Texto de prueba");
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}

