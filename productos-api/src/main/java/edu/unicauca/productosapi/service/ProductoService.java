/**
 * @file ProductoService.java
 * @brief Contrato del servicio de productos.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.service;

import java.util.List;

import edu.unicauca.productosapi.dto.ProductoRequest;
import edu.unicauca.productosapi.dto.ProductoResponse;

/**
 * @brief Operaciones de negocio del CRUD de productos.
 */
public interface ProductoService {

    /**
     * @brief Lista todos los productos.
     * @return Productos registrados
     */
    List<ProductoResponse> listarTodos();

    /**
     * @brief Busca un producto.
     * @param id Identificador
     * @return Producto encontrado
     * @throws ProductoNoEncontradoException Si no existe
     */
    ProductoResponse buscarPorId(Long id);

    /**
     * @brief Registra un producto.
     * @param producto Datos validados del producto
     * @return Producto guardado, con su id
     */
    ProductoResponse crear(ProductoRequest producto);

    /**
     * @brief Reemplaza los datos de un producto.
     * @param id Identificador
     * @param producto Datos nuevos, validados
     * @return Producto actualizado
     * @throws ProductoNoEncontradoException Si no existe
     */
    ProductoResponse actualizar(Long id, ProductoRequest producto);

    /**
     * @brief Elimina un producto.
     * @param id Identificador
     * @throws ProductoNoEncontradoException Si no existe
     */
    void eliminar(Long id);
}
