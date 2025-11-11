package com.esameIts.CharmApp.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.esameIts.CharmApp.dto.UserDto;
import com.esameIts.CharmApp.entities.Role;
import com.esameIts.CharmApp.entities.User;
import com.esameIts.CharmApp.mappers.UserMapper;
import com.esameIts.CharmApp.repositories.RoleRepository;
import com.esameIts.CharmApp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	
	    private final UserRepository userRepository;
	    private final RoleRepository roleRepository;
	    
	    /**
	     * Restituisce tutti gli utenti
	     */
	    public List<UserDto> getAllUsers() {
	        return userRepository.findAll().stream()
	            .map(UserMapper::toDto)
	            .toList();
	    }

	    /**
	     * Restituisce un utente per ID
	     */
	    public UserDto getUserById(Long id) {
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + id));
	        return UserMapper.toDto(user);
	    }

	    /**
	     * Crea un nuovo utente
	     */
	    public UserDto createUser(UserDto dto) {
	        User user = new User();
	        user.setName(dto.getName());
	        user.setEmail(dto.getEmail());
	        user.setEnabled(dto.isEnabled());

	        // Converto i nomi dei ruoli in entità Role
	        if (dto.getRoles() != null) {
	            Set<Role> roles = dto.getRoles().stream()
	                .map(roleName -> roleRepository.findByName(roleName)
	                    .orElseThrow(() -> new RuntimeException("Ruolo non trovato: " + roleName)))
	                .collect(Collectors.toSet());
	            user.setRoles(roles);
	        }

	        User saved = userRepository.save(user);
	        return UserMapper.toDto(saved);
	    }

	    /**
	     * Aggiorna un utente esistente
	     */
	    public UserDto updateUser(Long id, UserDto dto) {
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + id));

	        user.setName(dto.getName());
	        user.setEmail(dto.getEmail());
	        user.setEnabled(dto.isEnabled());

	        if (dto.getRoles() != null) {
	            Set<Role> roles = dto.getRoles().stream()
	                .map(roleName -> roleRepository.findByName(roleName)
	                    .orElseThrow(() -> new RuntimeException("Ruolo non trovato: " + roleName)))
	                .collect(Collectors.toSet());
	            user.setRoles(roles);
	        }

	        User updated = userRepository.save(user);
	        return UserMapper.toDto(updated);
	    }

	    /**
	     * Elimina un utente
	     */
	    public void deleteUser(Long id) {
	        if (!userRepository.existsById(id)) {
	            throw new RuntimeException("Utente non trovato con id: " + id);
	        }
	        userRepository.deleteById(id);
	    }
	}


