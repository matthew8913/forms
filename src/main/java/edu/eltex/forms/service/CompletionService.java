package edu.eltex.forms.service;

import edu.eltex.forms.dto.CompletionRequestDTO;
import edu.eltex.forms.dto.CompletionResponseDTO;
import edu.eltex.forms.entities.Completion;
import edu.eltex.forms.mapper.CompletionMapper;
import edu.eltex.forms.repository.CompletionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CompletionService {

    private final CompletionRepository completionRepository;
    private final CompletionMapper completionMapper;

    public CompletionService(CompletionRepository completionRepository,
                             CompletionMapper completionMapper) {
        this.completionRepository = completionRepository;
        this.completionMapper = completionMapper;
    }

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
        // Save won't work without setting valid references and cascade save
        // All child must have references to CORRESPONDING parents (comp <- answer <- opt)
        // Ignoring = NullPointerException or Hibernate Exception
        completionEntity.getAnswers().forEach(answer -> {
            answer.setCompletion(completionEntity);
            if (answer.getSelectedOption() != null) {
                answer.getSelectedOption().setQuestion(answer.getQuestion());
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
