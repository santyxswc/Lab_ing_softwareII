/**
 * @file ApplicationConfig.java
 * @brief Registra los casos de uso (Java puro) como beans de Spring.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.config;

import co.edu.unicauca.saberpro.question.application.QuestionService;
import co.edu.unicauca.saberpro.question.application.port.JustificationValidator;
import co.edu.unicauca.saberpro.question.application.port.ReviewCommandPublisher;
import co.edu.unicauca.saberpro.question.domain.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.question.domain.port.QuestionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @brief Inyección de dependencias manual de la capa de aplicación.
 *
 * Así {@link QuestionService} no necesita anotaciones de Spring.
 */
@Configuration
public class ApplicationConfig {

    /**
     * @brief Crea el servicio de preguntas.
     * @param validator  Adaptador REST hacia Validation Service.
     * @param repository Adaptador JPA.
     * @param publisher  Adaptador RabbitMQ.
     * @return Caso de uso listo para el controlador.
     */
    @Bean
    public QuestionService questionService(JustificationValidator validator,
                                           QuestionRepository repository,
                                           ReviewCommandPublisher publisher) {
        return new QuestionService(QuestionBuilder::new, validator, repository, publisher);
    }
}
