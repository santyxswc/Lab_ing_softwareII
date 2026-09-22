package edu.unicauca.productosapi.service;

import java.util.List;

import edu.unicauca.productosapi.model.Producto;

public interface ProductoService {

    List<Producto> listarTodos();

    Producto buscarPorId(Long id);

    Producto crear(Producto producto);

    Producto actualizar(Long id, Producto producto);

    void eliminar(Long id);
}
