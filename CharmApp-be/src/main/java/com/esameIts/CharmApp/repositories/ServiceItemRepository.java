package com.esameIts.CharmApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esameIts.CharmApp.entities.ServiceItem;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    List<ServiceItem> findByCategoryIdAndActiveTrue(Long categoryId);

}