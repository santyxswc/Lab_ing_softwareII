/**
 * @file JpaQuestionAdapterTest.java
 * @brief Pruebas de integración para el adaptador de persistencia JPA (JpaQuestionAdapter con H2).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure;

import co.edu.unicauca.saberpro.domain.model.Enunciado;
import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Justificacion;
import co.edu.unicauca.saberpro.domain.model.OpcionRespuesta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.JpaQuestionAdapter;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.mapper.PreguntaJpaMapper;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.repository.SpringDataPreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Infraestructura :: Adaptador de Salida - JpaQuestionAdapter (H2)")
class JpaQuestionAdapterTest {

    @Autowired
    private SpringDataPreguntaRepository repository;

    private JpaQuestionAdapter adapter;

    @BeforeEach
    void setUp() {
        PreguntaJpaMapper mapper = new PreguntaJpaMapper();
        adapter = new JpaQuestionAdapter(repository, mapper);
    }

    private Pregunta crearPreguntaDominio(String texto) {
        Pregunta p = Pregunta.crearBorrador(
            new Enunciado(texto),
            new Justificacion("Justificación técnica para prueba de persistencia JPA."),
            List.of(
                new OpcionRespuesta("Clave A", true),
                new OpcionRespuesta("Clave B", false),
                new OpcionRespuesta("Clave C", false),
                new OpcionRespuesta("Clave D", false)
            )
        );
        p.enviarARevision();
        return p;
    }

    @Test
    @DisplayName("Debe persistir en base de datos relacional H2 y asignar ID generado")
    void debeGuardarEnBaseDeDatosH2() {
        Pregunta pregunta = crearPreguntaDominio("¿Qué patrón pertenece a los patrones GoF estructurales?");

        Pregunta guardada = adapter.save(pregunta);

        assertNotNull(guardada.getId());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, guardada.getEstado());
        assertEquals(4, guardada.getOpciones().size());
        assertEquals("Clave A", guardada.getOpcionCorrecta().texto());
    }

    @Test
    @DisplayName("Debe consultar por ID y reconstruir el Agregado de dominio fielmente desde H2")
    void debeConsultarPorId() {
        Pregunta guardada = adapter.save(crearPreguntaDominio("¿Qué es DDD?"));

        Optional<Pregunta> encontrada = adapter.findById(guardada.getId());

        assertTrue(encontrada.isPresent());
        assertEquals(guardada.getId(), encontrada.get().getId());
        assertEquals(guardada.getEnunciado().valor(), encontrada.get().getEnunciado().valor());
        assertEquals(4, encontrada.get().getOpciones().size());
    }
}

