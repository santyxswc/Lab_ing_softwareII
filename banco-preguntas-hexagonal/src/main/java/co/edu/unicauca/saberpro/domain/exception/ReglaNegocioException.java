/**
 * @file ReglaNegocioException.java
 * @brief Excepción de dominio lanzada cuando se violan invariantes o reglas de negocio.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.domain.exception;

/**
 * Excepción propia del dominio que encapsula las violaciones a las reglas de negocio
 * del Banco de Preguntas Saber PRO (DDD Puro).
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}

