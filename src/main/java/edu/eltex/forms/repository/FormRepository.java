package edu.eltex.forms.repository;

import edu.eltex.forms.entities.Form;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FormRepository extends CrudRepository<Form, Integer> {

    Optional<Form> findByTitle(String title);
}
