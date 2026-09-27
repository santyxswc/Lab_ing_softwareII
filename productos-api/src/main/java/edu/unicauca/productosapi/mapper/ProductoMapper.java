/**
 * @file ProductoMapper.java
 * @brief Conversión entre DTOs y la entidad Producto.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.mapper;

import edu.unicauca.productosapi.dto.ProductoRequest;
import edu.unicauca.productosapi.dto.ProductoResponse;
import edu.unicauca.productosapi.model.Producto;

/**
 * @brief Traduce entre el contrato de la API (DTOs) y el modelo persistente.
 */
public final class ProductoMapper {

    /**
     * @brief Clase de utilidades: no se instancia.
     */
    private ProductoMapper() {
    }

    /**
     * @brief Crea una entidad nueva, sin id, a partir de la petición.
     * @param request Datos validados
     * @return Entidad lista para guardar
     */
    public static Producto aEntidad(ProductoRequest request) {
        return new Producto(request.nombre().trim(), request.descripcion(), request.precio(), request.stock());
    }

    /**
     * @brief Copia los datos de la petición sobre una entidad existente.
     * @param request Datos validados
     * @param producto Entidad a modificar
     */
    public static void actualizar(ProductoRequest request, Producto producto) {
        producto.setNombre(request.nombre().trim());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
    }

    /**
     * @brief Convierte una entidad en la respuesta de la API.
     * @param producto Entidad
     * @return DTO de respuesta
     */
    public static ProductoResponse aRespuesta(Producto producto) {
        return new ProductoResponse(producto.getId(), producto.getNombre(), producto.getDescripcion(),
                producto.getPrecio(), producto.getStock());
    }
}
