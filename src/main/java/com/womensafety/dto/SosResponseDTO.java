package com.womensafety.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SosResponseDTO {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String address;
    private LocalDateTime triggeredAt;
    private Boolean contactsNotified;
    private int contactsCount;
}
