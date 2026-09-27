/**
 * @file ProductoRequest.java
 * @brief Datos de entrada para crear o actualizar un producto.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * @brief Cuerpo JSON de POST y PUT /api/productos.
 *
 * No incluye el id: lo asigna la base de datos al crear y lo indica la ruta al actualizar,
 * así un cliente no puede sobrescribir otro registro enviando un id en el cuerpo.
 *
 * @param nombre Nombre (obligatorio, hasta 100 caracteres)
 * @param descripcion Descripción opcional (hasta 500 caracteres)
 * @param precio Precio unitario mayor que cero, con hasta 2 decimales
 * @param stock Unidades disponibles (0 o más)
 */
public record ProductoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre admite hasta 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción admite hasta 500 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
        @Digits(integer = 12, fraction = 2, message = "El precio admite hasta 2 decimales")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock) {
}
