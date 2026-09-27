/**
 * @file ProductoResponse.java
 * @brief Representación de un producto en las respuestas de la API.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.dto;

import java.math.BigDecimal;

/**
 * @brief Producto tal como lo ve el cliente de la API; desacopla el contrato HTTP de la entidad JPA.
 *
 * @param id Identificador
 * @param nombre Nombre
 * @param descripcion Descripción
 * @param precio Precio unitario
 * @param stock Unidades disponibles
 */
public record ProductoResponse(Long id, String nombre, String descripcion, BigDecimal precio, Integer stock) {
}
