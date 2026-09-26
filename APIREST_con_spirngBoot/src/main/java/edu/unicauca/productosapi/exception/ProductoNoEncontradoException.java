/**
 * @file ProductoNoEncontradoException.java
 * @brief Excepción de producto inexistente.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.exception;

/**
 * @brief Se lanza cuando se pide un producto que no existe; la API responde 404.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    /**
     * @brief Crea la excepción con el id buscado en el mensaje.
     * @param id Identificador que no se encontró
     */
    public ProductoNoEncontradoException(Long id) {
        super("No se encontro el producto con id " + id);
    }
}
