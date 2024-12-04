package edu.eltex.forms.service;

import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.entities.Form;
import edu.eltex.forms.mapper.FormMapper;
import edu.eltex.forms.model.FormModel;
import edu.eltex.forms.repository.FormRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;

    @Autowired
    public FormService(FormRepository formRepository, FormMapper formMapper) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
    }

    public FormResponseDTO createForm(FormRequestDTO dto) {
        FormModel formModel = formMapper.toModel(dto);
        Form formEntity = formMapper.toEntity(formModel);
        formEntity = formRepository.save(formEntity);
        return formMapper.toDto(formEntity);
    }

    public List<FormResponseDTO> getAllForms() {
        return StreamSupport.stream(formRepository.findAll().spliterator(), false)
                .map(formMapper::toDto)
                .toList();
    }

    public FormResponseDTO getFormById(Integer id) {
        Form formEntity = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found"));
        return formMapper.toDto(formEntity);
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
