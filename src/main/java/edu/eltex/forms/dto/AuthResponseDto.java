package edu.eltex.forms.dto;

import edu.eltex.forms.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private User.Role role;
    private Long userId;
    private String username;
}
