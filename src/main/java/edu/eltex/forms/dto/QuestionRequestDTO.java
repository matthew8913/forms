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
public class QuestionRequestDTO {

    private Integer id;

    private Integer formId;

    @NotNull(message = "Text is mandatory")
    private String text;

    @NotNull(message = "Type is mandatory")
    private String type;

    private String imageUrl;

    private List<OptionRequestDTO> options;
}
