/**
 * @file InMemoryQuestionAdapter.java
 * @brief Adaptador de salida (Driven / Outbound) en memoria con ConcurrentHashMap.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.memory;

import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.PreguntaRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @brief Adaptador de salida que implementa {@link PreguntaRepositoryPort}
 *        usando una estructura en memoria (ConcurrentHashMap).
 *
 * Demuestra la intercambiabilidad de adaptadores de la Arquitectura Hexagonal:
 * se activa cambiando la propiedad {@code saberpro.repository.adapter=memory}
 * sin tocar ni una línea del dominio.
 */
public class InMemoryQuestionAdapter implements PreguntaRepositoryPort {

    private final Map<Long, Question> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1L);

    @Override
    public Question save(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("La pregunta a guardar no puede ser nula.");
        }

        Question toStore = question;
        if (toStore.getId() == null) {
            Long nuevoId = idSequence.getAndIncrement();
            toStore = Question.restore(nuevoId, toStore.getStatement(), toStore.getJustification(),
                    toStore.getOptions(), toStore.getStatus(), toStore.getCreatedAt());
        }

        storage.put(toStore.getId(), toStore);
        return toStore;
    }

    @Override
    public Optional<Question> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(storage.values());
    }

    /**
     * @brief Limpia el almacén en memoria (útil para pruebas).
     */
    public void clear() {
        storage.clear();
        idSequence.set(1L);
    }
}
