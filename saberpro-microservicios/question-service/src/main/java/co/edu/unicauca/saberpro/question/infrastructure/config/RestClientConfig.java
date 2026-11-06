/**
 * @file RestClientConfig.java
 * @brief Configura el RestTemplate usado para llamar a Validation Service.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @brief Crea el RestTemplate con tiempos de espera acotados.
 */
@Configuration
public class RestClientConfig {

    /**
     * @brief RestTemplate con timeouts para no colgar la petición del usuario.
     * @param builder Builder provisto por Spring Boot.
     * @return Cliente HTTP.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(5))
                .build();
    }
}
