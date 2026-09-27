/**
 * @file ProductoServiceImpl.java
 * @brief Implementación del servicio de productos.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.unicauca.productosapi.exception.ProductoNoEncontradoException;
import edu.unicauca.productosapi.dto.ProductoRequest;
import edu.unicauca.productosapi.dto.ProductoResponse;
import edu.unicauca.productosapi.mapper.ProductoMapper;
import edu.unicauca.productosapi.model.Producto;
import edu.unicauca.productosapi.repository.ProductoRepository;

/**
 * @brief Implementa el CRUD de productos sobre ProductoRepository.
 *
 * Las lecturas usan transacciones de solo lectura y las escrituras una transacción cada una.
 */
@Service
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    /** Repositorio de productos. */
    private final ProductoRepository productoRepository;

    /**
     * @brief Crea el servicio con el repositorio inyectado por Spring.
     * @param productoRepository Repositorio de productos
     */
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * @brief Lista todos los productos.
     * @return Productos registrados
     */
    @Override
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream().map(ProductoMapper::aRespuesta).toList();
    }

    /**
     * @brief Busca un producto.
     * @param id Identificador
     * @return Producto encontrado
     * @throws ProductoNoEncontradoException Si no existe
     */
    @Override
    public ProductoResponse buscarPorId(Long id) {
        return ProductoMapper.aRespuesta(obtener(id));
    }

    /**
     * @brief Registra un producto.
     * @param producto Datos validados del producto
     * @return Producto guardado, con su id
     */
    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest producto) {
        return ProductoMapper.aRespuesta(productoRepository.save(ProductoMapper.aEntidad(producto)));
    }

    /**
     * @brief Reemplaza los datos de un producto.
     * @param id Identificador
     * @param producto Datos nuevos, validados
     * @return Producto actualizado
     * @throws ProductoNoEncontradoException Si no existe
     */
    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest producto) {
        Producto existente = obtener(id);
        ProductoMapper.actualizar(producto, existente);
        return ProductoMapper.aRespuesta(productoRepository.save(existente));
    }

    /**
     * @brief Elimina un producto.
     * @param id Identificador
     * @throws ProductoNoEncontradoException Si no existe
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        productoRepository.delete(obtener(id));
    }

    /**
     * @brief Busca la entidad o falla con 404.
     * @param id Identificador
     * @return Entidad encontrada
     * @throws ProductoNoEncontradoException Si no existe
     */
    private Producto obtener(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new ProductoNoEncontradoException(id));
    }
}
