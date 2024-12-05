package edu.eltex.forms.service;

import edu.eltex.forms.dto.CompletionRequestDTO;
import edu.eltex.forms.dto.CompletionResponseDTO;
import edu.eltex.forms.entities.Completion;
import edu.eltex.forms.mapper.CompletionMapper;
import edu.eltex.forms.repository.CompletionRepository;
import edu.eltex.forms.repository.OptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CompletionService {

    private final OptionRepository optionRepository;
    private final CompletionRepository completionRepository;
    private final CompletionMapper completionMapper;

    public List<CompletionResponseDTO> getAllCompletions() {
        return StreamSupport.stream(completionRepository.findAll().spliterator(), false)
                .map(completionMapper::toDTO)
                .toList();
    }

    public CompletionResponseDTO getCompletionById(Integer id) {
        Completion completionEntity = completionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Completion with given ID not found"));
        return completionMapper.toDTO(completionEntity);
    }

    public CompletionResponseDTO createCompletion(CompletionRequestDTO completionRequestDTO) {
        Completion completionEntity = completionMapper.toEntity(completionRequestDTO);
        // Save won't work without setting valid references and cascade
        // All child must have references to corresponding parents (not nulls or randomly generated)
        // Ignoring = NullPointerException or Hibernate Exception
        completionEntity.getAnswers().forEach(answer -> {
            answer.setCompletion(completionEntity);
            if (answer.getSelectedOptions() != null) {
                // Everything except options is new and needed to be saved, but options are not
                // Replace options by already existing ones to stop creating new options on every answer
                // Ignoring = Creating options on every POST and bad statistics
                var existingOptions = answer.getSelectedOptions().stream()
                        .map(option -> optionRepository.findByTextAndQuestion(option.getText(), answer.getQuestion()))
                        .toList();
                answer.setSelectedOptions(existingOptions);
            }
        });
        Completion completionEntitySaved = completionRepository.save(completionEntity);
        return completionMapper.toDTO(completionEntitySaved);
    }

    public boolean deleteCompletion(Integer id) {
        Optional<Completion> completionEntity = completionRepository.findById(id);
        if (completionEntity.isPresent()) {
            completionRepository.delete(completionEntity.get());
            return true;
        }
        return false;
    }
}
