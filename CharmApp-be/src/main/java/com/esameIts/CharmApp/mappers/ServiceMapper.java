package com.esameIts.CharmApp.mappers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.esameIts.CharmApp.dto.ServiceDto;
import com.esameIts.CharmApp.entities.ServiceItem;

public final class ServiceMapper {

    private ServiceMapper() {}

    public static ServiceDto toDto(ServiceItem e) {
        if (e == null) return null;
        return new ServiceDto(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getDurationMin(),
                e.getBufferMin(),
                e.getActive(),
                e.getPrice(),
                e.getImageUrl()
        );
    }

    public static List<ServiceDto> toDtoList(List<ServiceItem> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().filter(Objects::nonNull)
                .map(ServiceMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public static ServiceItem toEntity(ServiceDto dto) {
        if (dto == null) return null;
        ServiceItem s = new ServiceItem();
        s.setId(dto.getId());
        s.setName(dto.getName());
        s.setDescription(dto.getDescription());
        s.setDurationMin(dto.getDurationMin());
        s.setPrice(dto.getPrice());
        s.setActive(dto.getActive());
        s.setImageUrl(dto.getImageUrl());
        s.setBufferMin(dto.getBufferMin());
        // imposta categoria se nel dto hai l’id
        return s;
    }

}