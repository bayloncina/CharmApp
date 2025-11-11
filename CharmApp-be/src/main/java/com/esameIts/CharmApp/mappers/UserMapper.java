package com.esameIts.CharmApp.mappers;

import java.util.stream.Collectors;

import com.esameIts.CharmApp.dto.UserDto;
import com.esameIts.CharmApp.entities.Role;
import com.esameIts.CharmApp.entities.User;

public class UserMapper {

	private UserMapper() {}
	
	public static UserDto toDto(User u) {
	    return new UserDto(
	        u.getId(),
	        u.getName(),
	        u.getEmail(),
	        u.getRoles().stream()
	            .map(Role::getName)
	            .collect(Collectors.toSet()),
	        u.isEnabled()
	    );
	}
}
