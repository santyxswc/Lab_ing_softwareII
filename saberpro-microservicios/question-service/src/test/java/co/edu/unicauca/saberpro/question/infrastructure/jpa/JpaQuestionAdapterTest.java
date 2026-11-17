/**
 * @file JpaQuestionAdapterTest.java
 * @brief Prueba de integración del adaptador JPA sobre H2 (Punto 3.3).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.jpa;

import co.edu.unicauca.saberpro.question.domain.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.mapper.QuestionDomainMapper;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.record.QuestionRecord;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.repository.JpaQuestionAdapter;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.repository.SpringQuestionDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @brief Guarda y recupera una pregunta pasando por el mapeador y H2.
 *
 * Se desactiva la transacción de la prueba para que cada operación del
 * adaptador use su propia transacción, como en ejecución real.
 */
@DataJpaTest
@Import({JpaQuestionAdapter.class, QuestionDomainMapper.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class JpaQuestionAdapterTest {

    @Autowired
    private JpaQuestionAdapter repository;

    @Autowired
    private SpringQuestionDataRepository dataRepository;

    @Autowired
    private QuestionDomainMapper mapper;

    private static Question sample() {
        return new QuestionBuilder()
                .withStatement("¿Qué patrón separa construcción de representación?")
                .withJustification("El patrón Builder separa la construcción de su representación.")
                .withOption("A", "Singleton").withOption("B", "Builder")
                .withOption("C", "Observer").withOption("D", "State")
                .withCorrectAnswer("B")
                .build();
    }

    /** @brief La pregunta y sus 4 opciones se guardan en cascada y se leen igual. */
    @Test
    void savesAndLoadsWithCascade() {
        Question saved = repository.save(sample());
        assertNotNull(saved.getId());

        Question loaded = repository.findById(saved.getId()).orElseThrow();
        assertEquals(4, loaded.getOptions().size());
        assertEquals("B", loaded.getCorrectOption().getLabel());
        assertEquals(QuestionStatus.DRAFT, loaded.getStatus());
    }

    /** @brief Actualizar el estado no duplica las opciones. */
    @Test
    void updatingStatusKeepsOptions() {
        Question saved = repository.save(sample());
        saved.syncStatus(QuestionStatus.PENDING_REVIEW);
        repository.save(saved);

        Question loaded = repository.findById(saved.getId()).orElseThrow();
        assertEquals(QuestionStatus.PENDING_REVIEW, loaded.getStatus());
        assertEquals(4, loaded.getOptions().size());
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
