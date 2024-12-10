package edu.eltex.forms.controller;

import edu.eltex.forms.dto.*;
import edu.eltex.forms.service.AuthService;
import edu.eltex.forms.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequest) {
        AuthResponseDto authResponse = authService.getTokens(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<Void> registerUser(@RequestBody UserRequestDto registrationRequest) {
        authService.registerUser(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/refresh-token", consumes = "application/json")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshTokenResponseDto refreshTokenResponseDto = refreshTokenService.refreshAccessToken(refreshTokenRequestDto.getRefreshToken());
        return ResponseEntity.ok(refreshTokenResponseDto);
    }


    @PostMapping(value = "/logout", consumes = "application/json")
    public ResponseEntity<Void> logoutUser(@RequestBody LogoutRequestDto logoutRequestDto) {
        refreshTokenService.deleteRefreshToken(logoutRequestDto.getUsername());
        return ResponseEntity.ok().build();
    }
}
