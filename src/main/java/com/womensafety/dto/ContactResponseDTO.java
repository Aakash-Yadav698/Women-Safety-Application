package com.womensafety.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponseDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String relation;
}
