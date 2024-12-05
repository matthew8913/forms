package edu.eltex.forms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerModel {
    private Integer id;
    private CompletionModel completion;
    private QuestionModel question;
    private String answerText;
    private List<OptionModel> selectedOptions;
}
