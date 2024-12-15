package edu.eltex.forms.service;

import edu.eltex.forms.dto.AuthRequestDto;
import edu.eltex.forms.dto.AuthResponseDto;
import edu.eltex.forms.dto.UserRequestDto;
import edu.eltex.forms.dto.UserResponseDto;
import edu.eltex.forms.entities.User;
import edu.eltex.forms.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthResponseDto getTokens(AuthRequestDto authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtService.generateToken(userDetails);
        final String refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
        userService.saveRefreshToken(authRequest.getUsername(), refreshToken);
        UserResponseDto user = userService.findUserByUsername(authRequest.getUsername());
        UserRole role = user.getRole();
        String username = user.getUsername();
        Long id = Long.valueOf(user.getId());
        return new AuthResponseDto(jwt, refreshToken, role, id,username);
    }

    public boolean isAuthenticatedUserWithId(Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserResponseDto userResponseDto = userService.findUserById(userId);
            return userDetails.getUsername().equals(userResponseDto.getUsername());
        }
        return false;
    }

    public void registerUser(UserRequestDto registrationRequest) {
        userService.createUser(registrationRequest);
    }

    public Integer  getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByUsername(authentication.getName()).getId();
    }

}
