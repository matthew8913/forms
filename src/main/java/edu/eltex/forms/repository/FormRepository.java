package edu.eltex.forms.repository;

import edu.eltex.forms.entities.Form;
import org.springframework.data.repository.CrudRepository;

public interface FormRepository extends CrudRepository<Form, Integer> {
}
