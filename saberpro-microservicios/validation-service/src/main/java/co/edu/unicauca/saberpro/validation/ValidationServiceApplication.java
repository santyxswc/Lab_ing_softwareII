/**
 * @file ValidationServiceApplication.java
 * @brief Punto de entrada del microservicio Validation Service (:8082).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @brief Arranca el servicio que valida la justificación de los reactivos.
 *
 * Es invocado de forma síncrona (REST) por Question Service antes de guardar
 * una pregunta nueva.
 */
@SpringBootApplication
public class ValidationServiceApplication {

    /**
     * @brief Inicia la aplicación Spring Boot.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(ValidationServiceApplication.class, args);
    }
}
