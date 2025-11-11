package com.esameIts.CharmApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class BookingDto {
	
    private Long id;
    private Long serviceId;
    private String serviceName;
    private String userName;
    private String date;       // ISO: YYYY-MM-DD
    private String startTime;  // HH:mm
    private String endTime;    // HH:mm
    private String status;     // CONFIRMED / CANCELED
    private String notes;
}