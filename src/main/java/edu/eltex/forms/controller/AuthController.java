package edu.eltex.forms.controller;

import edu.eltex.forms.dto.*;
import edu.eltex.forms.service.AuthService;
import edu.eltex.forms.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> createAuthenticationToken(@RequestBody AuthRequestDto authRequest) {
        AuthResponseDto authResponse = authService.getTokens(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserRequestDto registrationRequest) {
        authService.registerUser(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshTokenResponseDto refreshTokenResponseDto = refreshTokenService.refreshAccessToken(refreshTokenRequestDto.getRefreshToken());
        return ResponseEntity.ok(refreshTokenResponseDto);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestBody LogoutRequestDto logoutRequestDto) {
        refreshTokenService.deleteRefreshToken(logoutRequestDto.getUsername());
        return ResponseEntity.ok().build();
    }
}
