package com.esameIts.CharmApp.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateServiceRequest {

	private String name;
    private String description;
    private int durationMin;
    private int bufferMin;
    private BigDecimal price;
    private String imageUrl;
}
