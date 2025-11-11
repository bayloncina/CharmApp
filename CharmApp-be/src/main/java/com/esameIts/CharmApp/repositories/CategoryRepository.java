package com.esameIts.CharmApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esameIts.CharmApp.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	 List<Category> findAllByOrderBySortAsc();
	 
	 
}
