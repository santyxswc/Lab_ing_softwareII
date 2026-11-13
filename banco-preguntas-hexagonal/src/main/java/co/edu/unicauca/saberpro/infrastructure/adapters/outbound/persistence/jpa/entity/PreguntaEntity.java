/**
 * @file PreguntaEntity.java
 * @brief Entidad JPA para la tabla 'preguntas'.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.entity;

import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de persistencia relacional que mapea la raíz del Agregado en H2.
 */
@Entity
@Table(name = "preguntas")
public class PreguntaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String enunciado;

    @Column(nullable = false, length = 2000)
    private String justificacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EstadoPregunta estado;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OpcionEntity> opciones = new ArrayList<>();

    public PreguntaEntity() {
    }

    public PreguntaEntity(Long id, String enunciado, String justificacion, EstadoPregunta estado) {
        this.id = id;
        this.enunciado = enunciado;
        this.justificacion = justificacion;
        this.estado = estado;
    }

    public void addOpcion(OpcionEntity opcion) {
        opciones.add(opcion);
        opcion.setPregunta(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    public void setEstado(EstadoPregunta estado) {
        this.estado = estado;
    }

    public List<OpcionEntity> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionEntity> opciones) {
        this.opciones = opciones;
    }
}

