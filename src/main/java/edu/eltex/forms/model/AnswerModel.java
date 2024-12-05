package edu.eltex.forms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerModel {
    private Integer id;
    private CompletionModel completion;
    private QuestionModel question;
    private String text;
    private OptionModel option;
}
