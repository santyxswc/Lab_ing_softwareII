/**
 * @file Producto.java
 * @brief Entidad Producto.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @brief Producto del inventario, persistido en la tabla productos.
 */
@Entity
@Table(name = "productos")
public class Producto {

    /** Identificador, generado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre. */
    private String nombre;

    /** Descripción. */
    private String descripcion;

    /** Precio unitario. */
    private Double precio;

    /** Unidades disponibles. */
    private Integer stock;

    /**
     * @brief Constructor vacío requerido por JPA.
     */
    public Producto() {
    }

    /**
     * @brief Crea un producto sin id (lo asigna la base de datos al guardarlo).
     * @param nombre Nombre
     * @param descripcion Descripción
     * @param precio Precio unitario
     * @param stock Unidades disponibles
     */
    public Producto(String nombre, String descripcion, Double precio, Integer stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    /**
     * @brief Obtiene el identificador.
     * @return Identificador, o null si aún no se ha guardado
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Asigna el identificador.
     * @param id Identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Obtiene el nombre.
     * @return Nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @brief Asigna el nombre.
     * @param nombre Nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @brief Obtiene la descripción.
     * @return Descripción
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @brief Asigna la descripción.
     * @param descripcion Descripción
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @brief Obtiene el precio.
     * @return Precio unitario
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * @brief Asigna el precio.
     * @param precio Precio unitario
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * @brief Obtiene el stock.
     * @return Unidades disponibles
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * @brief Asigna el stock.
     * @param stock Unidades disponibles
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
