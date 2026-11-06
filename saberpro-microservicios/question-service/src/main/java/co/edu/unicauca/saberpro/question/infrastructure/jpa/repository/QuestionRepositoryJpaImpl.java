/**
 * @file QuestionRepositoryJpaImpl.java
 * @brief Adaptador que implementa el puerto QuestionRepository con JPA + H2.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.jpa.repository;

import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.port.QuestionRepository;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.mapper.QuestionDomainMapper;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.record.QuestionRecord;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @brief Une el dominio con Spring Data (Punto 3.3).
 *
 * Recibe objetos de dominio, los convierte con {@link QuestionDomainMapper}
 * y los persiste con {@link SpringQuestionDataRepository}. Los métodos de
 * lectura son transaccionales para que el mapeo pueda recorrer las opciones
 * (relación LAZY) dentro de la sesión.
 */
@Repository
public class QuestionRepositoryJpaImpl implements QuestionRepository {

    private final SpringQuestionDataRepository dataRepository;
    private final QuestionDomainMapper mapper;

    /**
     * @brief Inyecta el repositorio Spring Data y el mapeador.
     * @param dataRepository Repositorio JPA generado por Spring.
     * @param mapper         Mapeador dominio ↔ JPA.
     */
    public QuestionRepositoryJpaImpl(SpringQuestionDataRepository dataRepository, QuestionDomainMapper mapper) {
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
