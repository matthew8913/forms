package edu.eltex.forms.service;

import edu.eltex.forms.dto.FormInfoResponseDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.entities.Form;
import edu.eltex.forms.mapper.FormMapper;
import edu.eltex.forms.model.FormModel;
import edu.eltex.forms.repository.FormRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class FormService {

    private final FormRepository formRepository;

    @Autowired
    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    public FormModel createForm(FormModel formModel) {
        if (formRepository.findByTitle(formModel.getTitle()).isPresent()) {
            throw new EntityExistsException("Form with title '" + formModel.getTitle() + "' already exists");
        }

        Form formEntity = FormMapper.INSTANCE.toEntity(formModel);
        formEntity = formRepository.save(formEntity);
        return FormMapper.INSTANCE.toModel(formEntity);
    }

    public List<FormInfoResponseDTO> getAllForms() {
        return StreamSupport.stream(formRepository.findAll().spliterator(), false)
                .map(FormMapper.INSTANCE::toInfoDto)
                .toList();
    }

    public List<FormResponseDTO> getAllFormsByCreatorName(String creatorName) {
        return formRepository.findAllByCreator_Username(creatorName).stream()
                .map(FormMapper.INSTANCE::toDto)
                .toList();
    }

    public FormResponseDTO getFormByTitle(String title) {
        Form formEntity = formRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Form with title '" + title + "' not found"));
        return FormMapper.INSTANCE.toDto(formEntity);
    }

    public FormResponseDTO getFormById(Integer id) {
        Form formEntity = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found"));
        return FormMapper.INSTANCE.toDto(formEntity);
    }

    public boolean deleteForm(Integer id) {
        Optional<Form> formEntity = formRepository.findById(id);
        if (formEntity.isPresent()) {
            formRepository.delete(formEntity.get());
            return true;
        }
        return false;
    }
}
