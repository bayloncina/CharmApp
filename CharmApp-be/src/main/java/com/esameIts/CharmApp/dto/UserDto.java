package com.esameIts.CharmApp.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private Long id;
    private String name;       // meglio usare "name" come da entity
    private String email;
    private Set<String> roles; // ruoli come stringhe
    private boolean enabled;   // allineato al campo entity
}



