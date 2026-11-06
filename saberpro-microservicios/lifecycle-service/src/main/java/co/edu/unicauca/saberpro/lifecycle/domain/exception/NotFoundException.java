/**
 * @file NotFoundException.java
 * @brief Se buscó algo que no existe.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.lifecycle.domain.exception;

/**
 * @brief Error de entidad no encontrada.
 */
public class NotFoundException extends DomainException {

    /**
     * @brief Construye el mensaje a partir del tipo y el id.
     * @param entity Nombre de la entidad.
     * @param id     Identificador buscado.
     */
    public NotFoundException(String entity, Object id) {
        super(entity + " con ID " + id + " no existe.");
    }
}
