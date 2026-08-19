package com.womensafety.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This is the whole point of the DTO layer: the User entity has a
// password field, this class simply does not. It is structurally
// impossible to leak the hash if you only ever return this type.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
}
