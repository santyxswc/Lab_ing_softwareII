/**
 * @file Question.java
 * @brief Entidad Question.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.common.entities;

/**
 * @brief Pregunta generada y almacenada en el banco del núcleo.
 */
public class Question {

    /** Identificador (UUID). */
    private String id;
    /** Título. */
    private String title;
    /** Contenido o enunciado. */
    private String content;
    /** Tipo: MULTIPLE_CHOICE, CASE_STUDY o MULTIMEDIA. */
    private String type;

    /**
     * @brief Crea una pregunta.
     * @param id Identificador
     * @param title Título
     * @param content Contenido
     * @param type Tipo
     */
    public Question(String id, String title, String content, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    /**
     * @brief Obtiene el identificador.
     * @return Identificador
     */
    public String getId() {
        return id;
    }

    /**
     * @brief Obtiene el título.
     * @return Título
     */
    public String getTitle() {
        return title;
    }

    /**
     * @brief Obtiene el contenido.
     * @return Contenido
     */
    public String getContent() {
        return content;
    }

    /**
     * @brief Obtiene el tipo.
     * @return Tipo
     */
    public String getType() {
        return type;
    }

    /**
     * @brief Representación corta de la pregunta.
     * @return Texto como "[MULTIPLE_CHOICE] Pregunta SOLID"
     */
    @Override
    public String toString() {
        return "[" + type + "] " + title;
    }
}
