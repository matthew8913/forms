package edu.eltex.forms.service;

import edu.eltex.forms.dto.UserRequestDto;
import edu.eltex.forms.dto.UserResponseDto;
import edu.eltex.forms.entities.User;
import edu.eltex.forms.mapper.UserMapper;
import edu.eltex.forms.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static edu.eltex.forms.enums.UserRole.USER;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserResponseDto findUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserResponseDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        userRequestDto.setRole(USER);
        User user = userMapper.toEntity(userRequestDto);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new EntityExistsException("User with username: " + user.getUsername() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        User updatedUser = userMapper.toEntity(userRequestDto);
        updatedUser.setId(id);
        User savedUser = userRepository.save(updatedUser);
        return userMapper.toDto(savedUser);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveRefreshToken(String username, String refreshToken) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}