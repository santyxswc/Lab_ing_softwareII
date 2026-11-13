/**
 * @file InMemoryQuestionAdapterTest.java
 * @brief Pruebas unitarias para el adaptador de persistencia en memoria (InMemoryQuestionAdapter).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure;

import co.edu.unicauca.saberpro.domain.model.Enunciado;
import co.edu.unicauca.saberpro.domain.model.Justificacion;
import co.edu.unicauca.saberpro.domain.model.OpcionRespuesta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.memory.InMemoryQuestionAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Infraestructura :: Adaptador de Salida - InMemoryQuestionAdapter")
class InMemoryQuestionAdapterTest {

    private InMemoryQuestionAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new InMemoryQuestionAdapter();
        adapter.clear();
    }

    private Pregunta crearPreguntaDePrueba(String textoEnunciado) {
        Pregunta p = Pregunta.crearBorrador(
            new Enunciado(textoEnunciado),
            new Justificacion("Justificación técnica de prueba suficiente"),
            List.of(
                new OpcionRespuesta("Opción 1", true),
                new OpcionRespuesta("Opción 2", false),
                new OpcionRespuesta("Opción 3", false),
                new OpcionRespuesta("Opción 4", false)
            )
        );
        p.enviarARevision();
        return p;
    }

    @Test
    @DisplayName("Debe asignar IDs autoincrementales al guardar reactivos en memoria")
    void debeGuardarYAsignarIdsAutoincrementales() {
        Pregunta p1 = crearPreguntaDePrueba("Pregunta número 1");
        Pregunta p2 = crearPreguntaDePrueba("Pregunta número 2");

        Pregunta g1 = adapter.save(p1);
        Pregunta g2 = adapter.save(p2);

        assertNotNull(g1.getId());
        assertNotNull(g2.getId());
        assertEquals(1L, g1.getId());
        assertEquals(2L, g2.getId());
    }

    @Test
    @DisplayName("Debe recuperar por ID un reactivo guardado en memoria")
    void debeRecuperarPorId() {
        Pregunta guardada = adapter.save(crearPreguntaDePrueba("Pregunta de prueba"));

        Optional<Pregunta> encontrada = adapter.findById(guardada.getId());

        assertTrue(encontrada.isPresent());
        assertEquals(guardada.getId(), encontrada.get().getId());
        assertEquals(guardada.getEnunciado().valor(), encontrada.get().getEnunciado().valor());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío si el ID no existe en memoria")
    void debeRetornarVacioSiNoExiste() {
        Optional<Pregunta> encontrada = adapter.findById(999L);
        assertTrue(encontrada.isEmpty());
    }

    @Test
    @DisplayName("Debe listar todas las preguntas guardadas en memoria")
    void debeListarTodas() {
        adapter.save(crearPreguntaDePrueba("Pregunta 1"));
        adapter.save(crearPreguntaDePrueba("Pregunta 2"));

        List<Pregunta> todas = adapter.findAll();

        assertEquals(2, todas.size());
    }
}

