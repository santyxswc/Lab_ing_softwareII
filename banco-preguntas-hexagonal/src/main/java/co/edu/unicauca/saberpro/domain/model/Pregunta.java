/**
 * @file Pregunta.java
 * @brief Raíz del Agregado (Aggregate Root) del Banco de Preguntas Saber PRO.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Raíz del Agregado (Aggregate Root) en Java puro sin dependencias de infraestructura.
 *
 * Encapsula el enunciado, la justificación técnica, las opciones de respuesta y
 * garantiza las invariantes del negocio antes de pasar al estado PENDIENTE_REVISION.
 */
public final class Pregunta {

    public static final int CANTIDAD_OPCIONES_EXIGIDAS = 4;
    public static final int CLAVES_VALIDAS_EXIGIDAS = 1;

    private final Long id;
    private final Enunciado enunciado;
    private final Justificacion justificacion;
    private final List<OpcionRespuesta> opciones;
    private EstadoPregunta estado;

    private Pregunta(Long id, Enunciado enunciado, Justificacion justificacion,
                     List<OpcionRespuesta> opciones, EstadoPregunta estado) {
        this.id = id;
        this.enunciado = Objects.requireNonNull(enunciado, "El enunciado no puede ser nulo.");
        this.justificacion = Objects.requireNonNull(justificacion, "La justificación no puede ser nula.");
        this.opciones = new ArrayList<>(Objects.requireNonNull(opciones, "La lista de opciones no puede ser nula."));
        this.estado = Objects.requireNonNull(estado, "El estado no puede ser nulo.");
    }

    /**
     * Crea un nuevo reactivo en estado BORRADOR (sin persistir, id nulo).
     *
     * @param enunciado     Texto del enunciado validado.
     * @param justificacion Sustentación técnica validada.
     * @param opciones      Opciones preliminares del reactivo.
     * @return Nueva instancia del Agregado Pregunta en estado BORRADOR.
     */
    public static Pregunta crearBorrador(Enunciado enunciado, Justificacion justificacion,
                                         List<OpcionRespuesta> opciones) {
        return new Pregunta(null, enunciado, justificacion, opciones, EstadoPregunta.BORRADOR);
    }

    /**
     * Reconstruye una pregunta desde la capa de persistencia (adaptadores).
     *
     * @param id            Identificador asignado en persistencia.
     * @param enunciado     Enunciado reconstruido.
     * @param justificacion Justificación reconstruida.
     * @param opciones      Opciones asociadas.
     * @param estado        Estado almacenado.
     * @return Instancia del Agregado con su estado histórico preservado.
     */
    public static Pregunta reconstruir(Long id, Enunciado enunciado, Justificacion justificacion,
                                       List<OpcionRespuesta> opciones, EstadoPregunta estado) {
        return new Pregunta(id, enunciado, justificacion, opciones, estado);
    }

    /**
     * Genera una copia del Agregado con el identificador asignado por el repositorio.
     *
     * @param nuevoId Identificador generado.
     * @return Nueva instancia con el ID asignado.
     */
    public Pregunta conId(Long nuevoId) {
        return new Pregunta(nuevoId, this.enunciado, this.justificacion, this.opciones, this.estado);
    }

    /**
     * Transiciona el reactivo al estado PENDIENTE_REVISION validando las invariantes de negocio:
     * 1. Deben existir exactamente 4 opciones de respuesta.
     * 2. Debe existir exactamente 1 sola clave marcada como correcta.
     *
     * Si no cumple alguna de las invariantes, lanza {@link ReglaNegocioException}.
     */
    public void enviarARevision() {
        if (opciones == null || opciones.size() != CANTIDAD_OPCIONES_EXIGIDAS) {
            int actual = (opciones == null) ? 0 : opciones.size();
            throw new ReglaNegocioException(
                "Invariante rota: El reactivo debe poseer exactamente " + CANTIDAD_OPCIONES_EXIGIDAS
                    + " opciones de respuesta para pasar a revisión (actual: " + actual + ")."
            );
        }

        long clavesCorrectas = opciones.stream().filter(OpcionRespuesta::esCorrecta).count();
        if (clavesCorrectas != CLAVES_VALIDAS_EXIGIDAS) {
            throw new ReglaNegocioException(
                "Invariante rota: El reactivo debe poseer exactamente " + CLAVES_VALIDAS_EXIGIDAS
                    + " clave válida antes de pasar a PENDIENTE_REVISION (actual: " + clavesCorrectas + ")."
            );
        }

        this.estado = EstadoPregunta.PENDIENTE_REVISION;
    }

    /**
     * Obtiene la opción identificada como correcta.
     *
     * @return La opción correcta.
     * @throws ReglaNegocioException si no existe o hay múltiples.
     */
    public OpcionRespuesta getOpcionCorrecta() {
        return opciones.stream()
            .filter(OpcionRespuesta::esCorrecta)
            .findFirst()
            .orElseThrow(() -> new ReglaNegocioException("La pregunta no contiene una clave correcta asignada."));
    }

    public Long getId() {
        return id;
    }

    public Enunciado getEnunciado() {
        return enunciado;
    }

    public Justificacion getJustificacion() {
        return justificacion;
    }

    public List<OpcionRespuesta> getOpciones() {
        return Collections.unmodifiableList(opciones);
    }

    public EstadoPregunta getEstado() {
        return estado;
    }
}

