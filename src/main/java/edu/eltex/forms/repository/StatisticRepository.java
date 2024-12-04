package edu.eltex.forms.repository;

import edu.eltex.forms.entities.Answer;
import edu.eltex.forms.entities.Option;
import edu.eltex.forms.entities.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatisticRepository extends CrudRepository<Answer, Integer> {

    @Query("SELECT COUNT(r) FROM Response r WHERE r.form.id = :form_id")
    Integer countNumberOfResponses(@Param("form_id") int formId);

    @Query("SELECT a FROM Answer a WHERE a.question.form.id = :form_id AND a.question.type = 'TEXT' ")
    List<Answer> getTextAnswers(@Param("form_id") int formId);

    @Query("SELECT a FROM Answer a WHERE a.question.form.id = :form_id AND a.question.type = 'NUMERIC'")
    List<Answer> getNumericAnswers(@Param("form_id") int formId);

    @Query("SELECT a FROM Answer a WHERE a.question.form.id = :form_id AND a.question.type in ('SINGLE_CHOICE', 'MULTIPLE_CHOICE') ")
    List<Answer> getChoisesAnswers(@Param("form_id")int formId);

    @Query("SELECT o FROM Option o WHERE o.question.form.id = :form_id AND o.question.type in ('SINGLE_CHOICE', 'MULTIPLE_CHOICE')")
    List<Option> getAllOptions(@Param("form_id") int formId);

    @Query("SELECT q FROM Question q WHERE q.form.id = :form_id ORDER BY q.id")
    List<Question> getAllQuestions(@Param("form_id") int formId);
}