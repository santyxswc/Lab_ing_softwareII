/**
 * @file ProductoControllerTest.java
 * @brief Pruebas de la capa web: rutas, códigos de estado, validación y formato de errores.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import edu.unicauca.productosapi.dto.ProductoResponse;
import edu.unicauca.productosapi.exception.ProductoNoEncontradoException;
import edu.unicauca.productosapi.service.ProductoService;

/**
 * @brief Levanta solo la capa web con el servicio simulado.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductoService servicio;

    @Test
    void crearResponde201ConLaUbicacionDelNuevoRecurso() throws Exception {
        when(servicio.crear(any())).thenReturn(new ProductoResponse(5L, "Lápiz", null, new BigDecimal("1200.00"), 30));

        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Lápiz\",\"precio\":1200,\"stock\":30}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/productos/5"))
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void datosInvalidosResponden400ConElErrorDeCadaCampo() throws Exception {
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\" \",\"precio\":-5,\"stock\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.title").value("Datos inválidos"))
                .andExpect(jsonPath("$.errores.nombre").value("El nombre es obligatorio"))
                .andExpect(jsonPath("$.errores.precio").value("El precio debe ser mayor que cero"))
                .andExpect(jsonPath("$.errores.stock").value("El stock no puede ser negativo"));

        verifyNoInteractions(servicio);
    }

    @Test
    void unProductoInexistenteResponde404EnFormatoProblemDetail() throws Exception {
        when(servicio.buscarPorId(42L)).thenThrow(new ProductoNoEncontradoException(42L));

        mvc.perform(get("/api/productos/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("No se encontró el producto con id 42"));
    }

    @Test
    void unIdNoPositivoResponde400() throws Exception {
        mvc.perform(get("/api/productos/0")).andExpect(status().isBadRequest());
        verifyNoInteractions(servicio);
    }

    @Test
    void eliminarResponde204Y404SiNoExiste() throws Exception {
        mvc.perform(delete("/api/productos/1")).andExpect(status().isNoContent());

        doThrow(new ProductoNoEncontradoException(2L)).when(servicio).eliminar(2L);
        mvc.perform(delete("/api/productos/2")).andExpect(status().isNotFound());
    }
}
