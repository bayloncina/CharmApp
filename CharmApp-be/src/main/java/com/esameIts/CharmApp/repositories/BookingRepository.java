package com.esameIts.CharmApp.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.esameIts.CharmApp.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{
	
	 List<Booking> findByUserIdOrderByDateDescStartTimeDesc(Long userId);

	    @Query("""
	      select count(b) > 0 from Booking b
	      where b.date = :date and b.status <> 'CANCELED'
	        and (:start < b.endTime and b.startTime < :end)
	    """)
	    boolean existsOverlap(@Param("date") LocalDate date,
	                          @Param("start") LocalTime start,
	                          @Param("end") LocalTime end);

		List<Booking> findActiveByDate(LocalDate date);

	    
	}