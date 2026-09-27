/**
 * @file ProductoServiceImplTest.java
 * @brief Pruebas unitarias del servicio de productos.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.unicauca.productosapi.dto.ProductoRequest;
import edu.unicauca.productosapi.dto.ProductoResponse;
import edu.unicauca.productosapi.exception.ProductoNoEncontradoException;
import edu.unicauca.productosapi.model.Producto;
import edu.unicauca.productosapi.repository.ProductoRepository;

/**
 * @brief Verifica la lógica del servicio con el repositorio simulado (sin base de datos).
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    /** Repositorio simulado. */
    @Mock
    private ProductoRepository repositorio;

    /** Servicio bajo prueba. */
    @InjectMocks
    private ProductoServiceImpl servicio;

    /**
     * @brief Producto ya guardado en la base.
     * @param id Id
     * @param nombre Nombre
     * @return Entidad con id
     */
    private static Producto guardado(long id, String nombre) {
        Producto p = new Producto(nombre, "desc", new BigDecimal("1500.00"), 10);
        p.setId(id);
        return p;
    }

    /**
     * @brief Listar devuelve cada entidad como ProductoResponse.
     */
    @Test
    void listarConvierteLasEntidadesEnRespuestas() {
        when(repositorio.findAll()).thenReturn(List.of(guardado(1, "Lápiz"), guardado(2, "Cuaderno")));

        assertThat(servicio.listarTodos()).extracting(ProductoResponse::nombre).containsExactly("Lápiz", "Cuaderno");
    }

    /**
     * @brief Crear guarda una entidad sin id y con el nombre sin espacios en los extremos.
     */
    @Test
    void crearGuardaUnaEntidadNuevaSinIdYRecortaElNombre() {
        when(repositorio.save(any())).thenAnswer(inv -> {
            Producto p = inv.getArgument(0);
            assertThat(p.getId()).as("la entidad llega al repositorio sin id").isNull();
            p.setId(7L);
            return p;
        });

        ProductoResponse creado = servicio.crear(new ProductoRequest("  Borrador ", null, new BigDecimal("800"), 5));

        assertThat(creado.id()).isEqualTo(7L);
        assertThat(creado.nombre()).isEqualTo("Borrador");
    }

    /**
     * @brief Actualizar copia los datos nuevos sobre el producto existente.
     */
    @Test
    void actualizarModificaElProductoExistente() {
        Producto existente = guardado(3, "Regla");
        when(repositorio.findById(3L)).thenReturn(Optional.of(existente));
        when(repositorio.save(existente)).thenReturn(existente);

        ProductoResponse actualizado = servicio.actualizar(3L, new ProductoRequest("Regla 30 cm", "Metálica", new BigDecimal("2500.50"), 4));

        assertThat(actualizado.id()).isEqualTo(3L);
        assertThat(actualizado.precio()).isEqualByComparingTo("2500.50");
        assertThat(existente.getDescripcion()).isEqualTo("Metálica");
    }

    /**
     * @brief Actualizar o eliminar un producto inexistente falla sin guardar ni borrar nada.
     */
    @Test
    void actualizarOEliminarUnProductoInexistenteFallaSinTocarLaBase() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());
        ProductoRequest datos = new ProductoRequest("X", null, BigDecimal.ONE, 1);

        assertThatThrownBy(() -> servicio.actualizar(99L, datos)).isInstanceOf(ProductoNoEncontradoException.class);
        assertThatThrownBy(() -> servicio.eliminar(99L)).isInstanceOf(ProductoNoEncontradoException.class)
                .hasMessageContaining("99");
        verify(repositorio, never()).save(any());
        verify(repositorio, never()).delete(any());
    }
}
