package edu.eltex.forms.controller;

import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.service.FormService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forms")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<FormResponseDTO> createForm(@RequestBody FormRequestDTO formRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(formService.createForm(formRequestDTO));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FormResponseDTO>> getAllForms() {
        List<FormResponseDTO> forms = formService.getAllForms();
        return ResponseEntity.ok(forms);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<FormResponseDTO> getForm(@PathVariable Integer id) {
        FormResponseDTO form = formService.getFormById(id);
        return form != null ? ResponseEntity.ok(form) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteForm(@PathVariable Integer id) {
        boolean deleted = formService.deleteForm(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Form with given ID not found");
    }
}
