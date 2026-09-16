package co.edu.unicauca.microkernel.common.entities;

/**
 * Entidad de dominio que representa una pregunta ya generada y
 * almacenada en el banco de preguntas del núcleo (QuestionMicrokernel).
 */
public class Question {

    private String id;
    private String title;
    private String content;
    private String type;

    public Question(String id, String title, String content, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[" + type + "] " + title;
    }
}
