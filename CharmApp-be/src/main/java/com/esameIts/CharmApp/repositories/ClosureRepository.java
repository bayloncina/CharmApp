package com.esameIts.CharmApp.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esameIts.CharmApp.entities.Closure;

public interface ClosureRepository extends JpaRepository<Closure, Long> {
    Optional<Closure> findByDate(LocalDate date);
}
