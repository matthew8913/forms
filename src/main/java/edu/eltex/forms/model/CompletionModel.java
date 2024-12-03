package edu.eltex.forms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompletionModel {
    private Integer id;
    private UserModel user;
    private FormModel form;
}
