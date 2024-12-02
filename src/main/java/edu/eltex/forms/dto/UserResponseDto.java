package edu.eltex.forms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Integer id;
    private String username;
    private String role;
}
