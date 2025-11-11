package com.esameIts.CharmApp.repositories;

import java.time.DayOfWeek;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esameIts.CharmApp.entities.WorkingHour;

public interface WorkingHourRepository extends JpaRepository<WorkingHour, Long> {
	
    Optional<WorkingHour> findByDay(DayOfWeek day);
}
