/**
 * @file ProductoRepository.java
 * @brief Repositorio de productos.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unicauca.productosapi.model.Producto;

/**
 * @brief Acceso a datos de Producto.
 *
 * Spring Data JPA genera la implementación (findAll, findById, save, delete...) a partir de esta interfaz.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
