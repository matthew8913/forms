package edu.eltex.forms.repository;

import edu.eltex.forms.entities.Option;
import edu.eltex.forms.entities.Question;
import org.springframework.data.repository.CrudRepository;

public interface OptionRepository extends CrudRepository<Option, Integer> {
    Option findByTextAndQuestion(String text, Question question);
}