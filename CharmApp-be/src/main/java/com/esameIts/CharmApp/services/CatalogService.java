package com.esameIts.CharmApp.services;

import com.esameIts.CharmApp.dto.CategoryDto;
import com.esameIts.CharmApp.dto.ServiceDto;
import com.esameIts.CharmApp.entities.Category;
import com.esameIts.CharmApp.entities.ServiceItem;
import com.esameIts.CharmApp.mappers.CategoryMapper;
import com.esameIts.CharmApp.mappers.ServiceMapper;
import com.esameIts.CharmApp.repositories.CategoryRepository;
import com.esameIts.CharmApp.repositories.ServiceItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogService {

    private final CategoryRepository categoryRepo;
    private final ServiceItemRepository serviceRepo;

    // ================== CATEGORY ==================

    public List<CategoryDto> getAllCategories() {
        return CategoryMapper.toDtoList(categoryRepo.findAllByOrderBySortAsc());
    }

    public CategoryDto getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .map(CategoryMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto dto) {
        Category entity = CategoryMapper.toEntity(dto);
        return CategoryMapper.toDto(categoryRepo.save(entity));
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category entity = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        entity.setName(dto.getName());
        entity.setImageUrl(dto.getImageUrl());

        return CategoryMapper.toDto(categoryRepo.save(entity));
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    // ================== SERVICE ==================

    public List<ServiceDto> getActiveServicesByCategory(Long categoryId) {
        return ServiceMapper.toDtoList(serviceRepo.findByCategoryIdAndActiveTrue(categoryId));
    }

    public ServiceDto getServiceById(Long categoryId, Long serviceId) {
        ServiceItem s = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Servizio non trovato"));

        if (!s.getCategory().getId().equals(categoryId)) {
            throw new RuntimeException("Il servizio non appartiene a questa categoria");
        }
        return ServiceMapper.toDto(s);
    }

    @Transactional
    public ServiceDto createService(Long categoryId, ServiceDto dto) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        ServiceItem entity = ServiceMapper.toEntity(dto);
        entity.setCategory(category); // 🔗 assegna la categoria

        return ServiceMapper.toDto(serviceRepo.save(entity));
    }

    @Transactional
    public ServiceDto updateService(Long categoryId, Long serviceId, ServiceDto dto) {
        ServiceItem entity = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Servizio non trovato"));

        if (!entity.getCategory().getId().equals(categoryId)) {
            throw new RuntimeException("Il servizio non appartiene a questa categoria");
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDurationMin(dto.getDurationMin());
        entity.setBufferMin(dto.getBufferMin());
        entity.setPrice(dto.getPrice());
        entity.setActive(dto.getActive());
        entity.setImageUrl(dto.getImageUrl());

        return ServiceMapper.toDto(serviceRepo.save(entity));
    }

    @Transactional
    public void deleteService(Long categoryId, Long serviceId) {
        ServiceItem entity = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Servizio non trovato"));

        if (!entity.getCategory().getId().equals(categoryId)) {
            throw new RuntimeException("Il servizio non appartiene a questa categoria");
        }

        serviceRepo.delete(entity);
    }
}
