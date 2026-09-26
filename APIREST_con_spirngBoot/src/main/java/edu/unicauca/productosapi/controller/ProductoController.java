/**
 * @file ProductoController.java
 * @brief Controlador REST del CRUD de productos.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.unicauca.productosapi.model.Producto;
import edu.unicauca.productosapi.service.ProductoService;

/**
 * @brief Expone las operaciones CRUD de Producto en /api/productos.
 *
 * Solo traduce peticiones HTTP a llamadas del servicio; la lógica está en ProductoService.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    /** Servicio con la lógica del CRUD. */
    private final ProductoService productoService;

    /**
     * @brief Crea el controlador con el servicio inyectado por Spring.
     * @param productoService Servicio de productos
     */
    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * @brief GET /api/productos: lista todos los productos.
     * @return 200 con la lista de productos
     */
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    /**
     * @brief GET /api/productos/{id}: busca un producto.
     * @param id Identificador del producto
     * @return 200 con el producto; 404 si no existe (ver ManejadorGlobalExcepciones)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    /**
     * @brief POST /api/productos: crea un producto.
     * @param producto Datos del producto en el cuerpo JSON
     * @return 201 con el producto creado y su id
     */
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * @brief PUT /api/productos/{id}: reemplaza los datos de un producto.
     * @param id Identificador del producto
     * @param producto Datos nuevos en el cuerpo JSON
     * @return 200 con el producto actualizado; 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    /**
     * @brief DELETE /api/productos/{id}: elimina un producto.
     * @param id Identificador del producto
     * @return 204 sin contenido; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
