/**
 * @file Paso2_InMemoryInterchangeabilityTest.java
 * @brief Prueba de integración para el Paso 2 y 3: Verificación con InMemoryQuestionAdapter activo.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure;

import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.memory.InMemoryQuestionAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "saberpro.repository.adapter=memory")
@DisplayName("Intercambiabilidad :: Paso 2 y 3 - Adaptador InMemory activo (HashMap)")
class Paso2_InMemoryInterchangeabilityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PreguntaRepositoryPort repositoryPort;

    @Test
    @DisplayName("Debe verificar que el bean inyectado sea una instancia de InMemoryQuestionAdapter")
    void debeTenerAdaptadorInMemoryActivo() {
        assertInstanceOf(InMemoryQuestionAdapter.class, repositoryPort,
            "El puerto de salida debe estar implementado por InMemoryQuestionAdapter.");
    }

    @Test
    @DisplayName("Paso 3: Crear reactivo vía REST y verificar comportamiento idéntico con almacenamiento en memoria")
    void debeCrearYConsultarPreguntaConMemoria() throws Exception {
        String jsonPeticion = """
            {
              "enunciado": "¿Qué es un Agregado en Domain-Driven Design (DDD)?",
              "justificacion": "Un Agregado es un clúster de objetos de dominio tratados como una sola unidad.",
              "opciones": [
                { "texto": "Un clúster de entidades y objetos de valor con una raíz", "esCorrecta": true },
                { "texto": "Una tabla de base de datos con triggers", "esCorrecta": false },
                { "texto": "Un controlador REST de Spring Boot", "esCorrecta": false },
                { "texto": "Un archivo de configuración YAML", "esCorrecta": false }
              ]
            }
            """;

        mockMvc.perform(post("/api/preguntas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPeticion))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.estado", is("PENDIENTE_REVISION")))
            .andExpect(jsonPath("$.opciones", hasSize(4)))
            .andExpect(jsonPath("$.enunciado", containsString("Agregado")));

        // Consultar por ID
        mockMvc.perform(get("/api/preguntas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.estado", is("PENDIENTE_REVISION")));
    }

    @Test
    @DisplayName("Debe mantener las mismas reglas de validación sin importar el adaptador de salida")
    void debeRechazarMenosDeCuatroOpciones() throws Exception {
        String jsonTresOpciones = """
            {
              "enunciado": "¿Pregunta con opciones insuficientes?",
              "justificacion": "Justificación válida con longitud técnica requerida.",
              "opciones": [
                { "texto": "A", "esCorrecta": true },
                { "texto": "B", "esCorrecta": false },
                { "texto": "C", "esCorrecta": false }
              ]
            }
            """;

        mockMvc.perform(post("/api/preguntas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTresOpciones))
            .andExpect(status().isBadRequest());
    }
}

