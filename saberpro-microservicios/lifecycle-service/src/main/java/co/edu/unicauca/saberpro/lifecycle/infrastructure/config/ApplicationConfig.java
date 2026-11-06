/**
 * @file ApplicationConfig.java
 * @brief Registra el caso de uso (Java puro) como bean de Spring.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.config;

import co.edu.unicauca.saberpro.lifecycle.application.LifecycleService;
import co.edu.unicauca.saberpro.lifecycle.application.port.StatusChangePublisher;
import co.edu.unicauca.saberpro.lifecycle.domain.port.LifecycleRepository;
import co.edu.unicauca.saberpro.lifecycle.domain.port.OperationLogRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @brief Inyección de dependencias manual de la capa de aplicación.
 */
@Configuration
public class ApplicationConfig {

    /**
     * @brief Crea el servicio de ciclo de vida.
     * @param lifecycles Adaptador JPA de ciclos de vida.
     * @param operations Adaptador JPA de operaciones.
     * @param publisher  Adaptador RabbitMQ.
     * @return Caso de uso.
     */
    @Bean
    public LifecycleService lifecycleService(LifecycleRepository lifecycles, OperationLogRepository operations,
                                             StatusChangePublisher publisher) {
        return new LifecycleService(lifecycles, operations, publisher);
    }
}
