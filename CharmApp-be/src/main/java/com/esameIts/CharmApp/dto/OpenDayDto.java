package com.esameIts.CharmApp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class OpenDayDto {
    private String date;     // YYYY-MM-DD
    private String openTime; // HH:mm (dopo override/chiusura)
    private String closeTime;// HH:mm
}
