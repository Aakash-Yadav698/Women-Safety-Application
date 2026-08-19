package com.womensafety.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SosRequest {

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;
}
