package edu.eltex.forms.controller;

import edu.eltex.forms.dto.CompletionRequestDTO;
import edu.eltex.forms.dto.CompletionResponseDTO;
import edu.eltex.forms.service.CompletionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/completions")
public class CompletionController {
    private final CompletionService completionService;

    public CompletionController(CompletionService completionService) {
        this.completionService = completionService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CompletionResponseDTO>> getAllCompletions() {
        List<CompletionResponseDTO> completions = completionService.getAllCompletions();
        return ResponseEntity.ok(completions);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<CompletionResponseDTO> getCompletion(@PathVariable Integer id) {
        CompletionResponseDTO completion = completionService.getCompletionById(id);
        return completion != null ? ResponseEntity.ok(completion) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CompletionResponseDTO> createCompletion(@Validated @RequestBody CompletionRequestDTO completionRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(completionService.createCompletion(completionRequestDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCompletion(@PathVariable Integer id) {
        boolean deleted = completionService.deleteCompletion(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Completion with given ID not found");
    }
}
