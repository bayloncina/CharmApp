package com.esameIts.CharmApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SlotDto {
    private String startTime; // "HH:mm"
    private String endTime;   // "HH:mm"
}