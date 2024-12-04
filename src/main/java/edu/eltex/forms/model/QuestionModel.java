package edu.eltex.forms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionModel {
    private Integer id;
    private String text;
    private String type;
    private String imageUrl;
}
