/**
 * @file ProductoApiIntegrationTest.java
 * @brief Pruebas de extremo a extremo de la API sobre H2.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @brief Recorre el CRUD completo con la base de datos H2 en memoria.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductoApiIntegrationTest {

    /** Cliente HTTP simulado. */
    @Autowired
    private MockMvc mvc;

    /** Lector de las respuestas JSON. */
    @Autowired
    private ObjectMapper json;

    /**
     * @brief Crea un producto por la API.
     * @param cuerpo JSON del producto
     * @return Id asignado
     */
    private long crear(String cuerpo) throws Exception {
        String respuesta = mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return json.readTree(respuesta).get("id").asLong();
    }

    /**
     * @brief Crea, consulta, actualiza y elimina un producto; después la consulta responde 404.
     */
    @Test
    void cicloCompletoCrearConsultarActualizarEliminar() throws Exception {
        long id = crear("{\"nombre\":\"Cuaderno\",\"descripcion\":\"100 hojas\",\"precio\":4500.50,\"stock\":12}");

        mvc.perform(get("/api/productos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precio").value(4500.50));

        mvc.perform(put("/api/productos/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Cuaderno grande\",\"precio\":5000,\"stock\":8}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cuaderno grande"));

        mvc.perform(delete("/api/productos/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/productos/" + id)).andExpect(status().isNotFound());
    }

    /**
     * @brief Un id en el cuerpo de un POST crea un producto nuevo y no modifica el existente.
     */
    @Test
    void unIdEnElCuerpoDelPostNoSobrescribeOtroProducto() throws Exception {
        long original = crear("{\"nombre\":\"Original\",\"precio\":100,\"stock\":1}");

        long nuevo = crear("{\"id\":" + original + ",\"nombre\":\"Intruso\",\"precio\":1,\"stock\":1}");

        assertThat(nuevo).isNotEqualTo(original);
        JsonNode intacto = json.readTree(mvc.perform(get("/api/productos/" + original))
                .andReturn().getResponse().getContentAsString());
        assertThat(intacto.get("nombre").asText()).isEqualTo("Original");
    }
}
