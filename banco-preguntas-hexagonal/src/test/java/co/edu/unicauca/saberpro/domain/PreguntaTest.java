/**
 * @file PreguntaTest.java
 * @brief Pruebas unitarias para la Raíz del Agregado Pregunta y sus invariantes de negocio.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dominio :: Raíz del Agregado - Pregunta (Invariantes y Ciclo de Vida)")
class PreguntaTest {

    private Enunciado enunciadoValido;
    private Justificacion justificacionValida;
    private List<OpcionRespuesta> cuatroOpcionesUnaCorrecta;

    @BeforeEach
    void setUp() {
        enunciadoValido = new Enunciado("¿Cuál es el beneficio principal de los Puertos y Adaptadores?");
        justificacionValida = new Justificacion("Permite aislar el núcleo de negocio de la infraestructura tecnológica.");

        cuatroOpcionesUnaCorrecta = List.of(
            new OpcionRespuesta("Independencia de frameworks y tecnología", true),
            new OpcionRespuesta("Mayor acoplamiento a la base de datos", false),
            new OpcionRespuesta("Uso obligatorio de microservicios", false),
            new OpcionRespuesta("Eliminación de pruebas unitarias", false)
        );
    }

    @Test
    @DisplayName("Debe instanciar el reactivo en estado inicial BORRADOR y con ID nulo")
    void debeIniciarEnEstadoBorrador() {
        Pregunta pregunta = Pregunta.crearBorrador(enunciadoValido, justificacionValida, cuatroOpcionesUnaCorrecta);

        assertNull(pregunta.getId());
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
        assertEquals(4, pregunta.getOpciones().size());
        assertEquals(enunciadoValido, pregunta.getEnunciado());
        assertEquals(justificacionValida, pregunta.getJustificacion());
    }

    @Test
    @DisplayName("Debe transicionar a PENDIENTE_REVISION cuando cumple todas las invariantes (4 opciones y 1 correcta)")
    void debeTransicionarAPendienteRevision() {
        Pregunta pregunta = Pregunta.crearBorrador(enunciadoValido, justificacionValida, cuatroOpcionesUnaCorrecta);

        pregunta.enviarARevision();

        assertEquals(EstadoPregunta.PENDIENTE_REVISION, pregunta.getEstado());
        assertEquals("Independencia de frameworks y tecnología", pregunta.getOpcionCorrecta().texto());
    }

    @Test
    @DisplayName("Debe fallar si intenta pasar a revisión con menos de 4 opciones (Invariante de cantidad)")
    void debeFallarConMenosDeCuatroOpciones() {
        List<OpcionRespuesta> tresOpciones = List.of(
            new OpcionRespuesta("Opción 1", true),
            new OpcionRespuesta("Opción 2", false),
            new OpcionRespuesta("Opción 3", false)
        );

        Pregunta pregunta = Pregunta.crearBorrador(enunciadoValido, justificacionValida, tresOpciones);

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
        assertTrue(ex.getMessage().contains("exactamente 4 opciones"));
    }

    @Test
    @DisplayName("Debe fallar si intenta pasar a revisión con más de 4 opciones (Invariante de cantidad)")
    void debeFallarConMasDeCuatroOpciones() {
        List<OpcionRespuesta> cincoOpciones = List.of(
            new OpcionRespuesta("Opción 1", true),
            new OpcionRespuesta("Opción 2", false),
            new OpcionRespuesta("Opción 3", false),
            new OpcionRespuesta("Opción 4", false),
            new OpcionRespuesta("Opción 5", false)
        );

        Pregunta pregunta = Pregunta.crearBorrador(enunciadoValido, justificacionValida, cincoOpciones);

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
        assertTrue(ex.getMessage().contains("exactamente 4 opciones"));
    }

    @Test
    @DisplayName("Debe fallar si intenta pasar a revisión sin ninguna clave correcta (Invariante de clave)")
    void debeFallarSinClaveCorrecta() {
        List<OpcionRespuesta> cuatroFalsas = List.of(
            new OpcionRespuesta("Opción 1", false),
            new OpcionRespuesta("Opción 2", false),
            new OpcionRespuesta("Opción 3", false),
            new OpcionRespuesta("Opción 4", false)
        );

        Pregunta pregunta = Pregunta.crearBorrador(enunciadoValido, justificacionValida, cuatroFalsas);

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
        assertTrue(ex.getMessage().contains("exactamente 1 clave válida"));
    }

    @Test
    @DisplayName("Debe fallar si intenta pasar a revisión con múltiples claves correctas (Invariante de clave)")
    void debeFallarConMultiplesClavesCorrectas() {
        List<OpcionRespuesta> dosCorrectas = List.of(
            new OpcionRespuesta("Opción 1", true),
            new OpcionRespuesta("Opción 2", true),
            new OpcionRespuesta("Opción 3", false),
            new OpcionRespuesta("Opción 4", false)
        );

        Pregunta pregunta = Pregunta.crearBorrador(enunciadoValido, justificacionValida, dosCorrectas);

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
        assertTrue(ex.getMessage().contains("exactamente 1 clave válida"));
    }

    @Test
    @DisplayName("La lista de opciones devuelta por el agregado debe ser inmutable para proteger el encapsulamiento")
    void debeProtegerEncapsulamientoDeOpciones() {
        Pregunta pregunta = Pregunta.crearBorrador(enunciadoValido, justificacionValida, cuatroOpcionesUnaCorrecta);
        List<OpcionRespuesta> opciones = pregunta.getOpciones();

        assertThrows(UnsupportedOperationException.class, () ->
            opciones.add(new OpcionRespuesta("Opción intrusa", false))
        );
    }

    @Test
    @DisplayName("conId() debe generar una nueva instancia con el ID asignado preservando el estado")
    void debeAsignarIdCorrectamente() {
        Pregunta pregunta = Pregunta.crearBorrador(enunciadoValido, justificacionValida, cuatroOpcionesUnaCorrecta);
        pregunta.enviarARevision();

        Pregunta conId = pregunta.conId(42L);

        assertEquals(42L, conId.getId());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, conId.getEstado());
        assertEquals(pregunta.getEnunciado(), conId.getEnunciado());
        assertEquals(pregunta.getJustificacion(), conId.getJustificacion());
    }

    @Test
    @DisplayName("reconstruir() debe recrear fielmente un agregado previamente persistido")
    void debeReconstruirAgregado() {
        Pregunta reconstruida = Pregunta.reconstruir(
            100L,
            enunciadoValido,
            justificacionValida,
            cuatroOpcionesUnaCorrecta,
            EstadoPregunta.PENDIENTE_REVISION
        );

        assertEquals(100L, reconstruida.getId());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, reconstruida.getEstado());
        assertEquals(4, reconstruida.getOpciones().size());
    }
}

