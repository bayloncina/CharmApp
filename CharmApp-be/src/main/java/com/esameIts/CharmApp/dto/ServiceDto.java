package com.esameIts.CharmApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private Long id;
    private String name;
    private String description;
    private int durationMin;
    private int bufferMin;
    private Boolean active;
    private BigDecimal price;
    private String imageUrl;
}