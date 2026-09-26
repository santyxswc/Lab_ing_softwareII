/**
 * @file ProductoServiceImpl.java
 * @brief Implementación del servicio de productos.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.unicauca.productosapi.exception.ProductoNoEncontradoException;
import edu.unicauca.productosapi.model.Producto;
import edu.unicauca.productosapi.repository.ProductoRepository;

/**
 * @brief Implementa el CRUD de productos sobre ProductoRepository.
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    /** Repositorio de productos. */
    private final ProductoRepository productoRepository;

    /**
     * @brief Crea el servicio con el repositorio inyectado por Spring.
     * @param productoRepository Repositorio de productos
     */
    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * @brief Lista todos los productos.
     * @return Productos registrados
     */
    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    /**
     * @brief Busca un producto.
     * @param id Identificador
     * @return Producto encontrado
     * @throws ProductoNoEncontradoException Si no existe
     */
    @Override
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    /**
     * @brief Registra un producto.
     * @param producto Datos del producto
     * @return Producto guardado, con su id
     */
    @Override
    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * @brief Copia los datos nuevos sobre el producto existente y lo guarda.
     * @param id Identificador
     * @param producto Datos nuevos
     * @return Producto actualizado
     * @throws ProductoNoEncontradoException Si no existe
     */
    @Override
    public Producto actualizar(Long id, Producto producto) {
        Producto existente = buscarPorId(id);
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        return productoRepository.save(existente);
    }

    /**
     * @brief Elimina un producto después de verificar que existe.
     * @param id Identificador
     * @throws ProductoNoEncontradoException Si no existe
     */
    @Override
    public void eliminar(Long id) {
        Producto existente = buscarPorId(id);
        productoRepository.delete(existente);
    }
}
