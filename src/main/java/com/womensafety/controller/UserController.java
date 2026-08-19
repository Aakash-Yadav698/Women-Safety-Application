package com.womensafety.controller;

import com.womensafety.dto.LocationUpdateRequest;
import com.womensafety.dto.UserResponseDTO;
import com.womensafety.security.AuthUtil;
import com.womensafety.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// Every endpoint here requires a valid JWT (SecurityConfig: anyRequest().authenticated())
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDTO getMyProfile() {
        Long userId = AuthUtil.getCurrentUserId();
        return userService.getProfile(userId);
    }

    @PutMapping("/me/location")
    public UserResponseDTO updateLocation(@Valid @RequestBody LocationUpdateRequest request) {
        Long userId = AuthUtil.getCurrentUserId();
        return userService.updateLocation(userId, request);
    }
}
