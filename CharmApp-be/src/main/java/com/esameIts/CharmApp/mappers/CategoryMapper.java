package com.esameIts.CharmApp.mappers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.esameIts.CharmApp.dto.CategoryDto;
import com.esameIts.CharmApp.entities.Category;

public final class CategoryMapper {

    private CategoryMapper() {}

    public static CategoryDto toDto(Category e) {
        if (e == null) return null;
        return new CategoryDto(
                e.getId(),
                e.getName(),
                e.getImageUrl()
        );
    }

    public static List<CategoryDto> toDtoList(List<Category> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().filter(Objects::nonNull)
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public static Category toEntity(CategoryDto dto) {
        if (dto == null) return null;
        Category c = new Category();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setImageUrl(dto.getImageUrl());
        return c;
    }
}