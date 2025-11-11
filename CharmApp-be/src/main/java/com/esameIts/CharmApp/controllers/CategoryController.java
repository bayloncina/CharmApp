package com.esameIts.CharmApp.controllers;

import com.esameIts.CharmApp.dto.CategoryDto;
import com.esameIts.CharmApp.dto.ServiceDto;
import com.esameIts.CharmApp.services.CatalogService;
import com.esameIts.CharmApp.services.StorageService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CatalogService catalogService;
    private final StorageService storageService;

    // ========= CATEGORIES =========

    @GetMapping
    public List<CategoryDto> categories() {
        return catalogService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return catalogService.getCategoryById(id);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public CategoryDto createCategory(@RequestBody CategoryDto dto) {
//        return catalogService.createCategory(dto);
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(
            @RequestPart("category") CategoryDto dto,
            @RequestPart("image") MultipartFile imageFile) {
        String imagePath = storageService.saveFile(imageFile);
        dto.setImageUrl(imagePath);
        return catalogService.createCategory(dto);
    }
    
//    @PutMapping("/{id}")
//    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
//        return catalogService.updateCategory(id, dto);
//    }
    
    @PutMapping("/{id}")
    public CategoryDto updateCategory(
            @PathVariable Long id,
            @RequestPart("category") CategoryDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = storageService.saveFile(imageFile);
            dto.setImageUrl(imagePath);
        }
        return catalogService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        catalogService.deleteCategory(id);
    }

    // ========= SERVICES =========

    @GetMapping("/{id}/services")
    public List<ServiceDto> servicesByCategory(@PathVariable Long id) {
        return catalogService.getActiveServicesByCategory(id);
    }

    @GetMapping("/{categoryId}/services/{serviceId}")
    public ServiceDto getService(
            @PathVariable Long categoryId,
            @PathVariable Long serviceId
    ) {
        return catalogService.getServiceById(categoryId, serviceId);
    }

    @PostMapping("/{id}/services")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDto createService(
            @PathVariable Long id,
            @RequestPart("service") ServiceDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = storageService.saveFile(imageFile);
            dto.setImageUrl(imagePath);
        }
        return catalogService.createService(id, dto);
    }

    @PutMapping("/{categoryId}/services/{serviceId}")
    public ServiceDto updateService(
            @PathVariable Long categoryId,
            @PathVariable Long serviceId,
            @RequestPart("service") ServiceDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = storageService.saveFile(imageFile);
            dto.setImageUrl(imagePath);
        }
        return catalogService.updateService(categoryId, serviceId, dto);
    }

    @DeleteMapping("/{categoryId}/services/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(
            @PathVariable Long categoryId,
            @PathVariable Long serviceId
    ) {
        catalogService.deleteService(categoryId, serviceId);
    }
}
