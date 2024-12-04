package edu.eltex.forms.repository;

import edu.eltex.forms.entities.Answer;
import edu.eltex.forms.entities.Option;
import edu.eltex.forms.entities.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatisticRepository extends CrudRepository<Answer, Integer> {

    @Query("SELECT COUNT(r) FROM Response r WHERE r.form.id = :formId")
    Integer countNumberOfResponses(@Param("formId") int formId);

    @Query("SELECT a FROM Answer a WHERE a.question.form.id = :formId AND a.question.type = 'TEXT' ")
    List<Answer> getTextAnswers(@Param("formId") int formId);

    @Query("SELECT a FROM Answer a WHERE a.question.form.id = :formId AND a.question.type = 'NUMERIC'")
    List<Answer> getNumericAnswers(@Param("formId") int formId);

    @Query("SELECT a FROM Answer a WHERE a.question.form.id = :formId AND a.question.type in ('SINGLE_CHOICE', 'MULTIPLE_CHOICE') ")
    List<Answer> getChoisesAnswers(@Param("formId")int formId);

    @Query("SELECT o FROM Option o WHERE o.question.form.id = :formId AND o.question.type in ('SINGLE_CHOICE', 'MULTIPLE_CHOICE')")
    List<Option> getAllOptions(@Param("formId") int formId);

    @Query("SELECT q FROM Question q WHERE q.form.id = :formId ORDER BY q.id")
    List<Question> getAllQuestions(@Param("formId") int formId);
}