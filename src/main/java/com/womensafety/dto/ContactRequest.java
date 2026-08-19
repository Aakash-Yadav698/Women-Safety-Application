package com.womensafety.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactRequest {

    @NotBlank(message = "Contact name is required")
    private String name;

    @NotBlank(message = "Contact phone number is required")
    private String phoneNumber;

    private String relation;
}
