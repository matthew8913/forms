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
public class CompletionRequestDTO {
    @NotNull(message = "User id is mandatory")
    private Integer userId;
    @NotNull(message = "Form id is mandatory")
    private Integer formId;
    @NotNull(message = "Answers is mandatory")
    List<AnswerRequestDTO> answers;
}
