package com.womensafety.service;

import com.womensafety.dto.*;
import com.womensafety.entity.User;
import com.womensafety.exception.BadRequestException;
import com.womensafety.exception.ResourceNotFoundException;
import com.womensafety.repository.UserRepository;
import com.womensafety.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponseDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        // NEVER store the raw password - hash it with BCrypt first
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());

        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return new LoginResponse(token, user.getId(), user.getFullName());
    }

    public UserResponseDTO getProfile(Long userId) {
        User user = findUserOrThrow(userId);
        return toDTO(user);
    }

    public UserResponseDTO updateLocation(Long userId, LocationUpdateRequest request) {
        User user = findUserOrThrow(userId);
        user.setLatitude(request.getLatitude());
        user.setLongitude(request.getLongitude());
        return toDTO(userRepository.save(user));
    }

    // package-private-ish helper other services reuse (e.g. SosAlertService
    // needs the raw User entity, not the DTO, to build the alert)
    public User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getLatitude(),
                user.getLongitude()
        );
    }
}
