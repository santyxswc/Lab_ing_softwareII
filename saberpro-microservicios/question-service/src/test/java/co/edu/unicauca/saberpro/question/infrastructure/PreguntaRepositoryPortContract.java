/**
 * @file PreguntaRepositoryPortContract.java
 * @brief Contrato común del puerto de persistencia (prueba de intercambiabilidad).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure;

import co.edu.unicauca.saberpro.question.domain.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.model.QuestionStatus;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @brief Las mismas pruebas se ejecutan contra cada adaptador del puerto.
 *
 * InMemoryQuestionAdapter y JpaQuestionAdapter heredan este contrato sin
 * cambiar una línea: si ambos lo cumplen, el dominio y la aplicación pueden
 * usar cualquiera de los dos (intercambiabilidad de la Arquitectura Hexagonal).
 */
public abstract class PreguntaRepositoryPortContract {

    /** @brief Adaptador bajo prueba; lo entrega cada subclase. */
    protected abstract PreguntaRepositoryPort port();

    /** @brief Pregunta válida de ejemplo, aún sin identificador. */
    protected static Question sample() {
        return new QuestionBuilder()
                .withStatement("¿Qué patrón separa construcción de representación?")
                .withJustification("El patrón Builder separa la construcción de su representación.")
                .withOption("A", "Singleton").withOption("B", "Builder")
                .withOption("C", "Observer").withOption("D", "State")
                .withCorrectAnswer("B")
                .build();
    }

    /** @brief Guardar asigna un id y la pregunta se recupera completa. */
    @Test
    void savesAndLoadsTheAggregate() {
        Question saved = port().save(sample());
        assertNotNull(saved.getId());

        Question loaded = port().findById(saved.getId()).orElseThrow();
        assertEquals(saved.getStatement(), loaded.getStatement());
        assertEquals(4, loaded.getOptions().size());
        assertEquals("B", loaded.getCorrectOption().getLabel());
        assertEquals(QuestionStatus.DRAFT, loaded.getStatus());
    }

    /** @brief Cada pregunta nueva recibe un id distinto y aparece en el listado. */
    @Test
    void assignsDistinctIdsAndListsThem() {
        Question first = port().save(sample());
        Question second = port().save(sample());

        assertNotEquals(first.getId(), second.getId());
        assertTrue(port().findAll().size() >= 2);
    }

    /** @brief Guardar una pregunta existente actualiza su estado sin duplicarla. */
    @Test
    void updatesStatusOfAnExistingQuestion() {
        Question saved = port().save(sample());
        saved.syncStatus(QuestionStatus.PENDING_REVIEW);
        port().save(saved);

        Question loaded = port().findById(saved.getId()).orElseThrow();
        assertEquals(QuestionStatus.PENDING_REVIEW, loaded.getStatus());
        assertEquals(4, loaded.getOptions().size());
    }

    /** @brief Un id inexistente devuelve vacío, no un error. */
    @Test
    void unknownIdIsEmpty() {
        assertTrue(port().findById(987_654L).isEmpty());
    }
}
