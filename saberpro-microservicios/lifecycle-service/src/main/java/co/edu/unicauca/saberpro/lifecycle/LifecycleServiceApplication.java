/**
 * @file LifecycleServiceApplication.java
 * @brief Punto de entrada del microservicio Lifecycle Service (:8084).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @brief Arranca el servicio que controla el ciclo de vida de las preguntas.
 */
@SpringBootApplication
public class LifecycleServiceApplication {

    /**
     * @brief Inicia la aplicación Spring Boot.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(LifecycleServiceApplication.class, args);
    }
}
