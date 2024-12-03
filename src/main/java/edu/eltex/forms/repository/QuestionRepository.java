package edu.eltex.forms.repository;

import edu.eltex.forms.entities.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
}
