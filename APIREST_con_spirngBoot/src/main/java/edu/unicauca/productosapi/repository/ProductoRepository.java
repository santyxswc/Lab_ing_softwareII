package edu.unicauca.productosapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unicauca.productosapi.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
