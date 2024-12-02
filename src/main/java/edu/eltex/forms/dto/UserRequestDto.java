package edu.eltex.forms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {
    private String username;
    private String password;
    private String role;
}