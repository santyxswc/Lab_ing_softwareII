/**
 * @file OpcionEntity.java
 * @brief Entidad JPA para la tabla 'opciones'.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.infrastructure.adapters.outbound.persistence.jpa.entity;

import jakarta.persistence.*;

/**
 * Entidad de persistencia relacional que almacena las opciones de respuesta
 * asociadas a una pregunta en H2.
 */
@Entity
@Table(name = "opciones")
public class OpcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String texto;

    @Column(name = "es_correcta", nullable = false)
    private boolean esCorrecta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id", nullable = false)
    private PreguntaEntity pregunta;

    public OpcionEntity() {
    }

    public OpcionEntity(Long id, String texto, boolean esCorrecta, PreguntaEntity pregunta) {
        this.id = id;
        this.texto = texto;
        this.esCorrecta = esCorrecta;
        this.pregunta = pregunta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    public PreguntaEntity getPregunta() {
        return pregunta;
    }

    public void setPregunta(PreguntaEntity pregunta) {
        this.pregunta = pregunta;
    }
}

