package com.womensafety.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String fullName;

    public LoginResponse(String token, Long userId, String fullName) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
    }
}
