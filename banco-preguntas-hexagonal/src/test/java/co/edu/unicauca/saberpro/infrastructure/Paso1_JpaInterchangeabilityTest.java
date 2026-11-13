/**
 * @file Paso1_JpaInterchangeabilityTest.java
 * @brief Prueba de integración para el Paso 1: Verificación con JpaQuestionAdapter activo.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure;

import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.JpaQuestionAdapter;
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
@TestPropertySource(properties = "saberpro.repository.adapter=jpa")
@DisplayName("Intercambiabilidad :: Paso 1 - Adaptador JPA activo (H2)")
class Paso1_JpaInterchangeabilityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PreguntaRepositoryPort repositoryPort;

    @Test
    @DisplayName("Debe verificar que el bean inyectado sea una instancia de JpaQuestionAdapter")
    void debeTenerAdaptadorJpaActivo() {
        assertInstanceOf(JpaQuestionAdapter.class, repositoryPort,
            "El puerto de salida debe estar implementado por JpaQuestionAdapter.");
    }

    @Test
    @DisplayName("Paso 1: Crear reactivo vía REST y verificar su transición a PENDIENTE_REVISION en JPA")
    void debeCrearYConsultarPreguntaConJpa() throws Exception {
        String jsonPeticion = """
            {
              "enunciado": "¿Qué es la Inversión de Dependencias en Arquitectura Hexagonal?",
              "justificacion": "El principio DIP permite que el dominio no dependa de adaptadores externos.",
              "opciones": [
                { "texto": "El dominio define puertos y la infraestructura los implementa", "esCorrecta": true },
                { "texto": "El dominio depende directamente de Hibernate y SQL", "esCorrecta": false },
                { "texto": "Todos los controladores llaman directamente a la base de datos", "esCorrecta": false },
                { "texto": "No se utilizan interfaces entre capas", "esCorrecta": false }
              ]
            }
            """;

        String respuestaJson = mockMvc.perform(post("/api/preguntas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPeticion))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.estado", is("PENDIENTE_REVISION")))
            .andExpect(jsonPath("$.opciones", hasSize(4)))
            .andReturn()
            .getResponse()
            .getContentAsString();

        // Verificar consulta GET
        mockMvc.perform(get("/api/preguntas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.estado", is("PENDIENTE_REVISION")));
    }

    @Test
    @DisplayName("Debe rechazar con 400 Bad Request si la justificación tiene menos de 15 caracteres")
    void debeRechazarJustificacionCorta() throws Exception {
        String jsonInvalido = """
            {
              "enunciado": "¿Pregunta de prueba?",
              "justificacion": "Corta",
              "opciones": [
                { "texto": "A", "esCorrecta": true },
                { "texto": "B", "esCorrecta": false },
                { "texto": "C", "esCorrecta": false },
                { "texto": "D", "esCorrecta": false }
              ]
            }
            """;

        mockMvc.perform(post("/api/preguntas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensaje", containsString("15 caracteres")));
    }

    @Test
    @DisplayName("Debe rechazar con 400 si se configuran dos claves correctas (invariante de negocio)")
    void debeRechazarDosClavesCorrectas() throws Exception {
        String jsonDosClaves = """
            {
              "enunciado": "¿Cuál es un beneficio de la Arquitectura Hexagonal?",
              "justificacion": "Sustentación técnica con longitud suficiente para cumplir regla.",
              "opciones": [
                { "texto": "Desacoplamiento", "esCorrecta": true },
                { "texto": "Testabilidad", "esCorrecta": true },
                { "texto": "Mayor acoplamiento", "esCorrecta": false },
                { "texto": "Uso obligatorio de cloud", "esCorrecta": false }
              ]
            }
            """;

        mockMvc.perform(post("/api/preguntas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDosClaves))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensaje", containsString("exactamente 1 clave válida")));
    }
}

