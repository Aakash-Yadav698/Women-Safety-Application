package com.womensafety.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private List<String> messages;

    public ApiErrorResponse(int status, String error, List<String> messages) {
        this.status = status;
        this.error = error;
        this.messages = messages;
    }
}
