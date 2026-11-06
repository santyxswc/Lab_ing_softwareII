/**
 * @file QuestionServiceApplication.java
 * @brief Punto de entrada del microservicio Question Service (:8083).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @brief Arranca el servicio que crea, guarda y envía a revisión las preguntas.
 */
@SpringBootApplication
public class QuestionServiceApplication {

    /**
     * @brief Inicia la aplicación Spring Boot.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(QuestionServiceApplication.class, args);
    }
}
