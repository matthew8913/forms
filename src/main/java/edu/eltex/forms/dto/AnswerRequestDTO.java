package edu.eltex.forms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequestDTO {

    @NotNull(message = "Question id is mandatory")
    private Integer completionId;
    private Integer questionId;
    private String answerText;
    private List<OptionRequestDTO> selectedOptions;
}