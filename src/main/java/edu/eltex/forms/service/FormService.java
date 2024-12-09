package edu.eltex.forms.service;

import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.entities.Form;
import edu.eltex.forms.mapper.FormMapper;
import edu.eltex.forms.repository.FormRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public FormResponseDTO createForm(FormRequestDTO formRequestDTO) {
        if (formRepository.findByTitle(formRequestDTO.getTitle()).isPresent()) {
            throw new EntityExistsException("Form with title '" + formRequestDTO.getTitle() + "' already exists");
        }

        Form formEntity = formMapper.toEntity(formRequestDTO);

        formEntity.getQuestions().forEach(question -> {
            question.setForm(formEntity);
            if (question.getOptions() != null) {
                question.getOptions().forEach(option -> option.setQuestion(question));
            }
        });

        Form createdFormEntity = formRepository.save(formEntity);
        return formMapper.toDto(createdFormEntity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<FormResponseDTO> getAllForms() {
        return StreamSupport.stream(formRepository.findAll().spliterator(), false)
                .map(formMapper::toDto)
                .toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<FormResponseDTO> getAllFormsByCreatorName(String creatorName) {
        return formRepository.findAllByCreator_Username(creatorName).stream()
                .map(formMapper::toDto)
                .toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FormResponseDTO getFormByTitle(String title) {
        Form formEntity = formRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Form with title '" + title + "' not found"));
        return formMapper.toDto(formEntity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FormResponseDTO getFormById(Integer id) {
        Form formEntity = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found"));
        return formMapper.toDto(formEntity);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean deleteForm(Integer id) {
        Optional<Form> formEntity = formRepository.findById(id);
        if (formEntity.isPresent()) {
            formRepository.delete(formEntity.get());
            return true;
        }
        return false;
    }
}
