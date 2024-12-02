package edu.eltex.forms.model;

import edu.eltex.forms.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormModel {

    private Integer id;
    private User creator;
    private String title;
    private String description;
}
