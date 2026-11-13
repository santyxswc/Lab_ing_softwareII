/**
 * @file JpaQuestionAdapter.java
 * @brief Adaptador de salida (Driven / Outbound) con Spring Data JPA y H2.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.entity.PreguntaEntity;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.mapper.PreguntaJpaMapper;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.repository.SpringDataPreguntaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de salida que implementa {@link PreguntaRepositoryPort} conectando el dominio
 * con una base de datos relacional H2 mediante Spring Data JPA.
 */
public class JpaQuestionAdapter implements PreguntaRepositoryPort {

    private final SpringDataPreguntaRepository springDataRepository;
    private final PreguntaJpaMapper mapper;

    public JpaQuestionAdapter(SpringDataPreguntaRepository springDataRepository,
                              PreguntaJpaMapper mapper) {
        this.springDataRepository = Objects.requireNonNull(springDataRepository, "springDataRepository no puede ser nulo.");
        this.mapper = Objects.requireNonNull(mapper, "mapper no puede ser nulo.");
    }

    @Override
    @Transactional
    public Pregunta save(Pregunta pregunta) {
        PreguntaEntity entity = mapper.toEntity(pregunta);
        PreguntaEntity savedEntity = springDataRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pregunta> findById(Long id) {
        return springDataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pregunta> findAll() {
        return springDataRepository.findAll().stream()
            .map(mapper::toDomain)
            .toList();
    }
}

