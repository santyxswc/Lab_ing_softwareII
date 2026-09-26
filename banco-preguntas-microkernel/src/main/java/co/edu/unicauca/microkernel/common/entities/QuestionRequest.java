/**
 * @file QuestionRequest.java
 * @brief Solicitud de generación de una pregunta.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package co.edu.unicauca.microkernel.common.entities;

import java.util.List;

/**
 * @brief Datos que la capa de presentación envía a los plugins para generar una Question.
 */
public class QuestionRequest {

    /** Título. */
    private String title;
    /** Contenido o enunciado. */
    private String content;
    /** Tipo de pregunta. */
    private String type;
    /** Área de conocimiento. */
    private String classification;
    /** Opciones de respuesta (solo selección múltiple). */
    private List<String> options;
    /** Respuesta correcta (solo selección múltiple). */
    private String correctAnswer;

    /**
     * @brief Crea la solicitud.
     * @param title Título
     * @param content Contenido
     * @param type Tipo de pregunta
     * @param classification Área de conocimiento
     * @param options Opciones de respuesta, o null
     * @param correctAnswer Respuesta correcta, o null
     */
    public QuestionRequest(String title, String content, String type, String classification,
                            List<String> options, String correctAnswer) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.classification = classification;
        this.options = options;
        this.correctAnswer = correctAnswer;
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
     * @return Tipo de pregunta
     */
    public String getType() {
        return type;
    }

    /**
     * @brief Obtiene el área de conocimiento.
     * @return Área de conocimiento
     */
    public String getClassification() {
        return classification;
    }

    /**
     * @brief Obtiene las opciones.
     * @return Opciones, o null si el tipo no las usa
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * @brief Obtiene la respuesta correcta.
     * @return Respuesta correcta, o null si el tipo no la usa
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
