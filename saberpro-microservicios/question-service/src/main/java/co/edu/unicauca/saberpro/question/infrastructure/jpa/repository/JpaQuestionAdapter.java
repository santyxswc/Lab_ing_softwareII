/**
 * @file JpaQuestionAdapter.java
 * @brief Adaptador de salida (Driven / Outbound) con Spring Data JPA y H2.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.jpa.repository;

import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.mapper.QuestionDomainMapper;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.record.QuestionRecord;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @brief Adaptador de salida que implementa {@link PreguntaRepositoryPort}
 *        conectando el dominio con H2 mediante Spring Data JPA.
 *
 * No lleva anotación de estereotipo: lo construye {@code RepositoryAdapterConfig}
 * como {@code @Bean} para poder intercambiarlo con el adaptador en memoria.
 */
public class JpaQuestionAdapter implements PreguntaRepositoryPort {

    private final SpringQuestionDataRepository dataRepository;
    private final QuestionDomainMapper mapper;

    /**
     * @brief Inyecta el repositorio Spring Data y el mapeador.
     * @param dataRepository Repositorio JPA generado por Spring.
     * @param mapper         Mapeador dominio ↔ JPA.
     */
    public JpaQuestionAdapter(SpringQuestionDataRepository dataRepository, QuestionDomainMapper mapper) {
        this.dataRepository = dataRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Question save(Question question) {
        QuestionRecord saved = dataRepository.save(mapper.toRecord(question));
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Question> findById(Long id) {
        return dataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return dataRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
