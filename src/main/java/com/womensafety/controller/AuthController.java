package com.womensafety.controller;

import com.womensafety.dto.LoginRequest;
import com.womensafety.dto.LoginResponse;
import com.womensafety.dto.RegisterRequest;
import com.womensafety.dto.UserResponseDTO;
import com.womensafety.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// These two endpoints are the only ones NOT requiring a JWT
// (see SecurityConfig: "/api/auth/**" is permitAll).
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequest request) {
        UserResponseDTO created = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
