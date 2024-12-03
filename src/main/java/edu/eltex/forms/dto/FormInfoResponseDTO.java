package edu.eltex.forms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormInfoResponseDTO {

    private Integer id;
    private Integer creatorId;
    private String creatorName;
    private String title;
    private String description;
}
