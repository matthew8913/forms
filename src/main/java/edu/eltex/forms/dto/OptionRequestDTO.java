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
public class OptionRequestDTO {

    private Integer id;

    private Integer questionId;

    @NotNull(message = "Text is mandatory")
    private String text;
}
