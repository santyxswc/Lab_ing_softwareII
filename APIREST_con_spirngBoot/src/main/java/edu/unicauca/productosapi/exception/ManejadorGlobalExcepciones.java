/**
 * @file ManejadorGlobalExcepciones.java
 * @brief Manejo centralizado de errores de la API.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @brief Convierte las excepciones de los controladores en respuestas HTTP con cuerpo JSON.
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    /**
     * @brief Responde 404 cuando un producto no existe.
     * @param ex Excepción lanzada por el servicio
     * @return Respuesta 404 con timestamp, status, error y message
     */
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarProductoNoEncontrado(ProductoNoEncontradoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
