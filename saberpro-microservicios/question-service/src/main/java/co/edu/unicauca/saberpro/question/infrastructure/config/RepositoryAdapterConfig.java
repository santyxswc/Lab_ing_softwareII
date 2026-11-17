/**
 * @file RepositoryAdapterConfig.java
 * @brief Configuración de la intercambiabilidad de adaptadores de persistencia.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.config;

import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.QuestionDomainMapper;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.JpaQuestionAdapter;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.SpringQuestionDataRepository;
import co.edu.unicauca.saberpro.question.infrastructure.memory.InMemoryQuestionAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @brief Desacopla e intercambia los adaptadores de salida que implementan
 *        {@link PreguntaRepositoryPort}.
 *
 * <ul>
 *   <li><b>JPA (por defecto):</b> activo cuando {@code saberpro.repository.adapter=jpa}
 *       o la propiedad no está definida.</li>
 *   <li><b>Memoria:</b> activo cuando {@code saberpro.repository.adapter=memory}.</li>
 * </ul>
 * Todo el hexágono (puertos y dominio) permanece intacto con cualquiera de los dos.
 */
@Configuration
public class RepositoryAdapterConfig {

    /**
     * @brief Adaptador JPA (activo por defecto).
     * @param repository Repositorio Spring Data.
     * @param mapper     Mapeador dominio ↔ JPA.
     * @return Puerto implementado por el adaptador JPA.
     */
    @Bean
    @ConditionalOnProperty(name = "saberpro.repository.adapter", havingValue = "jpa", matchIfMissing = true)
    public PreguntaRepositoryPort jpaQuestionAdapter(SpringQuestionDataRepository repository,
                                                     QuestionDomainMapper mapper) {
        return new JpaQuestionAdapter(repository, mapper);
    }

    /**
     * @brief Adaptador en memoria (activo con {@code saberpro.repository.adapter=memory}).
     * @return Puerto implementado por el adaptador en memoria.
     */
    @Bean
    @ConditionalOnProperty(name = "saberpro.repository.adapter", havingValue = "memory")
    public PreguntaRepositoryPort inMemoryQuestionAdapter() {
        return new InMemoryQuestionAdapter();
    }
}
