package edu.eltex.forms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponseDTO {

    private Integer id;
    private Integer completionId;
    private Integer questionId;
    private String answerText;
    private OptionResponseDTO selectedOption;
}
