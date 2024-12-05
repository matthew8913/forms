package edu.eltex.forms.service;

import edu.eltex.forms.dto.RefreshTokenResponseDto;
import edu.eltex.forms.entities.User;
import edu.eltex.forms.exception.TokenRefreshException;
import edu.eltex.forms.repository.UserRepository;
import edu.eltex.forms.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public String createRefreshToken(String username) {
        String refreshToken = UUID.randomUUID().toString();
        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new UsernameNotFoundException("User not found with username: " + username));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return refreshToken;
    }

    public RefreshTokenResponseDto refreshAccessToken(String refreshToken) {
        User user =
                userRepository
                        .findByRefreshToken(refreshToken)
                        .orElseThrow(
                                () -> new TokenRefreshException(refreshToken, "Refresh token not exists!"));
        if (user.getRefreshToken().equals(refreshToken)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            return new RefreshTokenResponseDto(jwtService.generateToken(userDetails));
        } else {
            throw new TokenRefreshException(refreshToken, "Refresh token is not valid!");
        }
    }

    public void deleteRefreshToken(String username) {
        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new UsernameNotFoundException("User not found with username: " + username));
        user.setRefreshToken(null);
        userRepository.save(user);
    }
}
