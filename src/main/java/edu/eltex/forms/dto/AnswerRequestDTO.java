package edu.eltex.forms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequestDTO {

    @NotNull(message = "Question id is mandatory")
    private Integer questionId;
    private Integer completionId;
    private String answerText;
    private OptionRequestDTO selectedOption;
}