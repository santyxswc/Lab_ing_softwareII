/**
 * @file SpringDataPreguntaRepository.java
 * @brief Repositorio Spring Data JPA para la entidad PreguntaEntity.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.repository;

import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.entity.PreguntaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de Spring Data JPA que provee las operaciones CRUD sobre la tabla 'preguntas'.
 */
@Repository
public interface SpringDataPreguntaRepository extends JpaRepository<PreguntaEntity, Long> {
}

