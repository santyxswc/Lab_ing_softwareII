/**
 * @file PreguntaJpaMapper.java
 * @brief Mapeador bidireccional entre el Agregado de Dominio y las Entidades JPA.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.mapper;

import co.edu.unicauca.saberpro.domain.model.Enunciado;
import co.edu.unicauca.saberpro.domain.model.Justificacion;
import co.edu.unicauca.saberpro.domain.model.OpcionRespuesta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.entity.OpcionEntity;
import co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.entity.PreguntaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Traduce entre el Agregado de dominio puro (sin anotaciones de base de datos)
 * y la entidad relacional JPA gestionada por Hibernate.
 */
@Component
public class PreguntaJpaMapper {

    /**
     * Convierte el Agregado de Dominio {@link Pregunta} a la entidad relacional {@link PreguntaEntity}.
     *
     * @param dominio Instancia pura del dominio.
     * @return Entidad JPA lista para persistir.
     */
    public PreguntaEntity toEntity(Pregunta dominio) {
        if (dominio == null) {
            return null;
        }

        PreguntaEntity entity = new PreguntaEntity(
            dominio.getId(),
            dominio.getEnunciado().valor(),
            dominio.getJustificacion().valor(),
            dominio.getEstado()
        );

        if (dominio.getOpciones() != null) {
            for (OpcionRespuesta opcion : dominio.getOpciones()) {
                OpcionEntity opcionEntity = new OpcionEntity(
                    null,
                    opcion.texto(),
                    opcion.esCorrecta(),
                    entity
                );
                entity.addOpcion(opcionEntity);
            }
        }

        return entity;
    }

    /**
     * Reconstruye el Agregado de Dominio {@link Pregunta} a partir de la entidad JPA.
     *
     * @param entity Entidad recuperada de la base de datos.
     * @return Agregado de dominio puro reconstruido.
     */
    public Pregunta toDomain(PreguntaEntity entity) {
        if (entity == null) {
            return null;
        }

        Enunciado enunciado = new Enunciado(entity.getEnunciado());
        Justificacion justificacion = new Justificacion(entity.getJustificacion());

        List<OpcionRespuesta> opciones = new ArrayList<>();
        if (entity.getOpciones() != null) {
            for (OpcionEntity opcionEntity : entity.getOpciones()) {
                opciones.add(new OpcionRespuesta(opcionEntity.getTexto(), opcionEntity.isEsCorrecta()));
            }
        }

        return Pregunta.reconstruir(
            entity.getId(),
            enunciado,
            justificacion,
            opciones,
            entity.getEstado()
        );
    }
}

