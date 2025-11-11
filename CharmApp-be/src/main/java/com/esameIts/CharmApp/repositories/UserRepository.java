package com.esameIts.CharmApp.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esameIts.CharmApp.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
	
}
