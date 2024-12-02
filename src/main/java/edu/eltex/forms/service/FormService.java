package edu.eltex.forms.service;

import edu.eltex.forms.dto.AnswerResponseDTO;
import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.dto.QuestionResponseDTO;
import edu.eltex.forms.entities.Form;
import edu.eltex.forms.mapper.AnswerMapper;
import edu.eltex.forms.mapper.FormMapper;
import edu.eltex.forms.mapper.QuestionMapper;
import edu.eltex.forms.model.FormModel;
import edu.eltex.forms.repository.FormRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    public FormService(FormRepository formRepository,
                       FormMapper formMapper,
                       QuestionMapper questionMapper,
                       AnswerMapper answerMapper) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
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
                .collect(Collectors.toList());
    }

    public FormResponseDTO getFormById(Integer id) {
        Form formEntity = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found"));

        FormResponseDTO formResponseDTO = formMapper.toDto(formEntity);

        List<QuestionResponseDTO> questionResponseDTOs = formEntity.getQuestions().stream().map(question -> {
            List<AnswerResponseDTO> questionAnswers = question.getAnswers().stream()
                    .filter(answer -> answer.getQuestion().equals(question))
                    .map(answerMapper::toDto)
                    .toList();

            QuestionResponseDTO questionDTO = questionMapper.toDto(question);
            questionDTO.setAnswers(questionAnswers);
            return questionDTO;
        }).collect(Collectors.toList());

        formResponseDTO.setQuestions(questionResponseDTOs);
        return formResponseDTO;
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
