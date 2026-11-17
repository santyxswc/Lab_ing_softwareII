/**
 * @file JpaQuestionAdapterTest.java
 * @brief Prueba de integración del adaptador JPA sobre H2 (Punto 3.3).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.jpa;

import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.question.infrastructure.PreguntaRepositoryPortContract;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @brief Ejecuta el contrato del puerto pasando por el mapeador y H2.
 *
 * Se desactiva la transacción de la prueba para que cada operación del
 * adaptador use su propia transacción, como en ejecución real.
 */
@DataJpaTest
@Import({JpaQuestionAdapter.class, QuestionDomainMapper.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class JpaQuestionAdapterTest extends PreguntaRepositoryPortContract {

    @Autowired
    private JpaQuestionAdapter repository;

    @Autowired
    private SpringQuestionDataRepository dataRepository;

    @Autowired
    private QuestionDomainMapper mapper;

    @Override
    protected PreguntaRepositoryPort port() {
        return repository;
    }

    /** @brief El mapeador es bidireccional: dominio → record → dominio conserva los datos. */
    @Test
    void mapperRoundTrip() {
        Question original = sample();
        QuestionRecord record = mapper.toRecord(original);
        assertEquals(4, record.getOptions().size());
        record.getOptions().forEach(o -> assertEquals(record, o.getQuestion()));

        Question back = mapper.toDomain(record);
        assertEquals(original.getStatement(), back.getStatement());
        assertEquals(original.getCorrectOption().getLabel(), back.getCorrectOption().getLabel());
    }
}
