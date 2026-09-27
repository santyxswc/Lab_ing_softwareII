/**
 * @file ProductoController.java
 * @brief Controlador REST del CRUD de productos.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import edu.unicauca.productosapi.dto.ProductoRequest;
import edu.unicauca.productosapi.dto.ProductoResponse;
import edu.unicauca.productosapi.service.ProductoService;

/**
 * @brief Expone las operaciones CRUD de Producto en /api/productos.
 *
 * Valida la entrada y elige el código de estado; los errores los traduce ManejadorGlobalExcepciones.
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
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * @brief GET /api/productos: lista todos los productos.
     * @return 200 con la lista de productos
     */
    @GetMapping
    public List<ProductoResponse> listarTodos() {
        return productoService.listarTodos();
    }

    /**
     * @brief GET /api/productos/{id}: busca un producto.
     * @param id Identificador del producto
     * @return 200 con el producto; 404 si no existe
     */
    @GetMapping("/{id}")
    public ProductoResponse buscarPorId(@PathVariable @Positive Long id) {
        return productoService.buscarPorId(id);
    }

    /**
     * @brief POST /api/productos: crea un producto.
     * @param producto Datos del producto en el cuerpo JSON
     * @return 201 con el producto creado y la cabecera Location; 400 si los datos no son válidos
     */
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest producto) {
        ProductoResponse creado = productoService.crear(producto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    /**
     * @brief PUT /api/productos/{id}: reemplaza los datos de un producto.
     * @param id Identificador del producto
     * @param producto Datos nuevos en el cuerpo JSON
     * @return 200 con el producto actualizado; 400 si los datos no son válidos; 404 si no existe
     */
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable @Positive Long id, @Valid @RequestBody ProductoRequest producto) {
        return productoService.actualizar(id, producto);
    }

    /**
     * @brief DELETE /api/productos/{id}: elimina un producto.
     * @param id Identificador del producto
     * @return 204 sin contenido; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
