/**
 * @file LifecycleRepositoryJpaImpl.java
 * @brief Adaptador JPA de los puertos LifecycleRepository y OperationLogRepository.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.repository;

import co.edu.unicauca.saberpro.lifecycle.domain.model.ProcessedOperation;
import co.edu.unicauca.saberpro.lifecycle.domain.model.QuestionLifecycle;
import co.edu.unicauca.saberpro.lifecycle.domain.port.LifecycleRepository;
import co.edu.unicauca.saberpro.lifecycle.domain.port.OperationLogRepository;
import co.edu.unicauca.saberpro.lifecycle.infrastructure.jpa.mapper.LifecycleDomainMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @brief Persiste en H2 usando los repositorios de Spring Data y el mapeador.
 */
@Repository
public class LifecycleRepositoryJpaImpl implements LifecycleRepository, OperationLogRepository {

    private final SpringLifecycleDataRepository lifecycleData;
    private final SpringOperationDataRepository operationData;
    private final LifecycleDomainMapper mapper;

    /**
     * @brief Inyecta dependencias.
     * @param lifecycleData Repositorio de ciclos de vida.
     * @param operationData Repositorio de operaciones.
     * @param mapper        Mapeador.
     */
    public LifecycleRepositoryJpaImpl(SpringLifecycleDataRepository lifecycleData,
                                      SpringOperationDataRepository operationData,
                                      LifecycleDomainMapper mapper) {
        this.lifecycleData = lifecycleData;
        this.operationData = operationData;
        this.mapper = mapper;
    }

    @Override
    public QuestionLifecycle save(QuestionLifecycle lifecycle) {
        return mapper.toDomain(lifecycleData.save(mapper.toRecord(lifecycle)));
    }

    @Override
    public Optional<QuestionLifecycle> findByQuestionId(Long questionId) {
        return lifecycleData.findById(questionId).map(mapper::toDomain);
    }

    @Override
    public void save(ProcessedOperation operation) {
        operationData.save(mapper.toRecord(operation));
    }

    @Override
    public Optional<ProcessedOperation> findById(String operationId) {
        return operationData.findById(operationId).map(mapper::toDomain);
    }

    @Override
    public boolean exists(String operationId) {
        return operationData.existsById(operationId);
    }
}
