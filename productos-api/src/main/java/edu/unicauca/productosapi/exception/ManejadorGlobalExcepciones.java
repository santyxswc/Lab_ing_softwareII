/**
 * @file ManejadorGlobalExcepciones.java
 * @brief Manejo centralizado de errores de la API.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @brief Convierte las excepciones en respuestas application/problem+json (RFC 9457).
 *
 * Todas las respuestas de error tienen la misma forma: type, title, status, detail e instance,
 * más un mapa "errores" campo → mensaje cuando la validación falla.
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones extends ResponseEntityExceptionHandler {

    /**
     * @brief 404 cuando un producto no existe.
     * @param ex Excepción lanzada por el servicio
     * @return Problema con estado 404
     */
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ProblemDetail manejarProductoNoEncontrado(ProductoNoEncontradoException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problema.setTitle("Producto no encontrado");
        return problema;
    }

    /**
     * @brief 400 con el detalle de cada campo inválido del cuerpo JSON.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Los datos del producto no son válidos");
        problema.setTitle("Datos inválidos");
        problema.setProperty("errores", errores);
        return ResponseEntity.badRequest().body(problema);
    }

    /**
     * @brief 400 cuando falla la validación de un parámetro de la ruta (por ejemplo, un id negativo).
     */
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "El identificador debe ser un número positivo");
        problema.setTitle("Parámetro inválido");
        return ResponseEntity.badRequest().body(problema);
    }
}
