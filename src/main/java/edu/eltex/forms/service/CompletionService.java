package edu.eltex.forms.service;

import edu.eltex.forms.dto.CompletionRequestDTO;
import edu.eltex.forms.dto.CompletionResponseDTO;
import edu.eltex.forms.entities.Completion;
import edu.eltex.forms.mapper.CompletionMapper;
import edu.eltex.forms.model.CompletionModel;
import edu.eltex.forms.repository.CompletionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    public CompletionResponseDTO getCompletionById(Integer id) {
        return null;
    }

    public CompletionResponseDTO createCompletion(CompletionRequestDTO completionRequestDTO) {
        CompletionModel completionModel = completionMapper.toModel(completionRequestDTO);
        Completion completionEntity = completionMapper.toEntity(completionModel);
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
