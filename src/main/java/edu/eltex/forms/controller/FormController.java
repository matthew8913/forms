package edu.eltex.forms.controller;

import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forms")
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    @Operation(summary = "Create new form")
    @PreAuthorize("hasRole('CREATOR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormResponseDTO> createForm(@RequestPart(name = "formRequestDTO") @Valid FormRequestDTO formRequestDTO,
                                                      @RequestPart(name = "images", required = false) List<MultipartFile> images) {
        if (formRequestDTO.getQuestions() != null && !formRequestDTO.getQuestions().isEmpty()) {
            for (int i = 0; i < formRequestDTO.getQuestions().size(); i++) {
                if (images!=null && i < images.size()) {
                    formRequestDTO.getQuestions().get(i).setImage(images.get(i));
                }
            }
        }

        FormResponseDTO formResponseDTO = formService.createForm(formRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(formResponseDTO);
    }

    @Operation(summary = "Find all existing forms")
    @PreAuthorize("hasRole('CREATOR') or hasRole('USER')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FormResponseDTO>> getAllForms() {
        List<FormResponseDTO> formResponseDTOS = formService.getAllForms();
        return ResponseEntity.ok(formResponseDTOS);
    }

    @Operation(summary = "Find form by specific ID")
    @PreAuthorize("hasRole('CREATOR') or hasRole('USER')")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<FormResponseDTO> getFormById(@PathVariable Integer id) {
        FormResponseDTO form = formService.getFormById(id);
        return form != null ? ResponseEntity.ok(form) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Find form by specific title")
    @PreAuthorize("hasRole('CREATOR') or hasRole('USER')")
    @GetMapping(produces = "application/json", params = "title")
    public ResponseEntity<FormResponseDTO> getFormByTitle(@RequestParam("title") String title) {
        FormResponseDTO form = formService.getFormByTitle(title);
        return form != null ? ResponseEntity.ok(form) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Find form by specific creator")
    @PreAuthorize("hasRole('CREATOR') or hasRole('USER')")
    @GetMapping(produces = "application/json", params = "username")
    public ResponseEntity<List<FormResponseDTO>> getAllFormsByCreatorName(@RequestParam("username") String username) {
        List<FormResponseDTO> formResponseDTOS = formService.getAllFormsByCreatorName(username);
        return ResponseEntity.ok(formResponseDTOS);
    }

    @Operation(summary = "Delete form by specific ID")
    @PreAuthorize("hasRole('CREATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteForm(@PathVariable Integer id) {
        boolean deleted = formService.deleteForm(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Form with given ID not found");
    }
}
