package edu.eltex.forms.controller;

import edu.eltex.forms.dto.*;
import edu.eltex.forms.mapper.FormMapper;
import edu.eltex.forms.mapper.OptionMapper;
import edu.eltex.forms.mapper.QuestionMapper;
import edu.eltex.forms.model.FormModel;
import edu.eltex.forms.model.OptionModel;
import edu.eltex.forms.model.QuestionModel;
import edu.eltex.forms.service.FormService;
import edu.eltex.forms.service.OptionService;
import edu.eltex.forms.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/forms")
public class FormController {

    private final FormService formService;
    private final QuestionService questionService;
    private final OptionService optionService;

    public FormController(FormService formService, QuestionService questionService, OptionService optionService) {
        this.formService = formService;
        this.questionService = questionService;
        this.optionService = optionService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<FormResponseDTO> createForm(@RequestBody FormRequestDTO formRequestDTO) {
        FormModel formModel = FormMapper.INSTANCE.toModel(formRequestDTO);
        FormModel createdFormModel = formService.createForm(formModel);
        FormResponseDTO formResponseDTO = FormMapper.INSTANCE.toDto(createdFormModel);

        List<QuestionResponseDTO> questionResponseDTOS = new ArrayList<>();
        for (QuestionRequestDTO questionRequestDTO : formRequestDTO.getQuestions()) {
            QuestionModel questionModel = QuestionMapper.INSTANCE.toModel(questionRequestDTO);
            questionModel.setFormId(createdFormModel.getId());

            QuestionModel createdQuestionModel = questionService.createQuestion(questionModel);

            List<OptionResponseDTO> optionResponseDTOS = new ArrayList<>();
            if (questionRequestDTO.getOptions() != null) {
                for (OptionRequestDTO optionRequestDTO : questionRequestDTO.getOptions()) {
                    OptionModel optionModel = OptionMapper.INSTANCE.toModel(optionRequestDTO);
                    optionModel.setQuestionId(createdQuestionModel.getId());
                    OptionModel createdOptionModel = optionService.createOption(optionModel);
                }
            }
            questionResponseDTOS.add(QuestionMapper.INSTANCE.toDto(createdQuestionModel));
        }

        formResponseDTO.setQuestions(questionResponseDTOS);

        return ResponseEntity.status(HttpStatus.CREATED).body(formResponseDTO);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FormInfoResponseDTO>> getAllForms() {
        List<FormInfoResponseDTO> formResponseDTOS = formService.getAllForms();
        return ResponseEntity.ok(formResponseDTOS);
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
