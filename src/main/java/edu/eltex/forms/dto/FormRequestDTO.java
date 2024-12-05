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
public class FormRequestDTO {

    private Integer id;

    @NotNull(message = "Creator id is mandatory")
    private Integer creatorId;

    @NotNull(message = "Creator name is mandatory")
    private String creatorName;

    @NotNull(message = "Title is mandatory")
    private String title;

    private String description;

    private List<QuestionRequestDTO> questions;
}
