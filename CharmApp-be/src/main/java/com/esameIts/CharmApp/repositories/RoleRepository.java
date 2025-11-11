package com.esameIts.CharmApp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esameIts.CharmApp.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	 Optional<Role> findByName(String name);

}
