package com.esameIts.CharmApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookingRequest {
    @NotNull
    private Long serviceId;

    @NotNull
    private String date;      // formato: YYYY-MM-DD

    @NotNull
    private String startTime; // formato: HH:mm

    private String notes;
}