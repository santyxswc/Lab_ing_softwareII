/**
 * @file RepositoryAdapterConfig.java
 * @brief Configuración de inyección de dependencias para la intercambiabilidad de adaptadores de persistencia.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.config;

import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.JpaQuestionAdapter;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.mapper.PreguntaJpaMapper;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.repository.SpringDataPreguntaRepository;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.memory.InMemoryQuestionAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración de infraestructura responsable del desacoplamiento e intercambiabilidad
 * de los adaptadores de salida (driven) que implementan {@link PreguntaRepositoryPort}.
 *
 * <p><b>Experimento de Intercambiabilidad (Taller 12):</b></p>
 * <ul>
 *   <li><b>Paso 1:</b> Activa {@link JpaQuestionAdapter} por defecto (base de datos relacional H2).</li>
 *   <li><b>Paso 2:</b> Permite conmutar a {@link InMemoryQuestionAdapter} cambiando la propiedad
 *       {@code saberpro.repository.adapter=memory} en {@code application.yml} o alternando
 *       la anotación {@code @Bean} directamente.</li>
 *   <li><b>Paso 3:</b> Todo el hexágono (puertos y dominio) permanece intacto y la API REST responde
 *       de forma idéntica con cualquiera de los dos adaptadores.</li>
 * </ul>
 */
@Configuration
public class RepositoryAdapterConfig {

    /**
     * Paso 1: Adaptador de Salida JPA (Activo por defecto si la propiedad es 'jpa' o no está definida).
     */
    @Bean
    @ConditionalOnProperty(name = "saberpro.repository.adapter", havingValue = "jpa", matchIfMissing = true)
    public PreguntaRepositoryPort jpaQuestionAdapter(
            SpringDataPreguntaRepository springDataRepository,
            PreguntaJpaMapper mapper) {
        return new JpaQuestionAdapter(springDataRepository, mapper);
    }

    /**
     * Paso 2: Adaptador de Salida en Memoria (Activo cuando la propiedad es 'memory').
     *
     * Para conmutar manualmente mediante anotaciones Java:
     * comente el bean {@code jpaQuestionAdapter} y descomente este bean (o use @Primary).
     */
    @Bean
    @ConditionalOnProperty(name = "saberpro.repository.adapter", havingValue = "memory")
    public PreguntaRepositoryPort inMemoryQuestionAdapter() {
        return new InMemoryQuestionAdapter();
    }
}

