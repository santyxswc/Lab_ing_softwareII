/**
 * @file QuestionDomainMapper.java
 * @brief Mapeador bidireccional entre Question (dominio) y QuestionRecord (JPA).
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.question.infrastructure.jpa.mapper;

import co.edu.unicauca.saberpro.question.domain.model.AnswerOption;
import co.edu.unicauca.saberpro.question.domain.model.Question;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.record.AnswerOptionRecord;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.record.QuestionRecord;
import org.springframework.stereotype.Component;

/**
 * @brief Traduce entre el modelo de dominio y el modelo relacional (Punto 3.3).
 *
 * Gracias a esta clase el dominio no conoce JPA y la base de datos no
 * conoce las reglas de negocio.
 */
@Component
public class QuestionDomainMapper {

    /**
     * @brief Dominio → JPA.
     * @param question Pregunta de dominio.
     * @return Entidad lista para guardar (con la relación bidireccional armada).
     */
    public QuestionRecord toRecord(Question question) {
        QuestionRecord record = new QuestionRecord();
        record.setId(question.getId());
        record.setStatement(question.getStatement());
        record.setJustification(question.getJustification());
        record.setStatus(question.getStatus());
        record.setCreatedAt(question.getCreatedAt());
        question.getOptions().forEach(o ->
                record.addOption(new AnswerOptionRecord(o.getId(), o.getLabel(), o.getText(), o.isCorrect())));
        return record;
    }

    /**
     * @brief JPA → dominio.
     * @param record Entidad leída de la base de datos.
     * @return Pregunta de dominio.
     */
    public Question toDomain(QuestionRecord record) {
        return Question.restore(
                record.getId(),
                record.getStatement(),
                record.getJustification(),
                record.getOptions().stream()
                        .map(o -> new AnswerOption(o.getId(), o.getLabel(), o.getText(), o.isCorrect()))
                        .toList(),
                record.getStatus(),
                record.getCreatedAt());
    }
}
