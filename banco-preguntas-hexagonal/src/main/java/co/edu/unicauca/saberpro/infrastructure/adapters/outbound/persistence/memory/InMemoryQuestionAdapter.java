/**
 * @file InMemoryQuestionAdapter.java
 * @brief Adaptador de salida (Driven / Outbound) en memoria basado en HashMap.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.memory;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Adaptador de salida que implementa {@link PreguntaRepositoryPort} utilizando
 * una estructura en memoria (HashMap / ConcurrentHashMap).
 *
 * Se utiliza para demostrar la intercambiabilidad de adaptadores en la
 * Arquitectura Hexagonal sin modificar ni una sola línea de código del dominio.
 */
public class InMemoryQuestionAdapter implements PreguntaRepositoryPort {

    private final Map<Long, Pregunta> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1L);

    @Override
    public Pregunta save(Pregunta pregunta) {
        if (pregunta == null) {
            throw new IllegalArgumentException("La pregunta a guardar no puede ser nula.");
        }

        Pregunta aGuardar = pregunta;
        if (aGuardar.getId() == null) {
            Long nuevoId = idSequence.getAndIncrement();
            aGuardar = aGuardar.conId(nuevoId);
        }

        storage.put(aGuardar.getId(), aGuardar);
        return aGuardar;
    }

    @Override
    public Optional<Pregunta> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Pregunta> findAll() {
        return new ArrayList<>(storage.values());
    }

    /**
     * Limpia el almacén en memoria (útil para pruebas unitarias e integración).
     */
    public void clear() {
        storage.clear();
        idSequence.set(1L);
    }
}

