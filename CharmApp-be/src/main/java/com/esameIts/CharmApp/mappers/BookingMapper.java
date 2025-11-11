package com.esameIts.CharmApp.mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.esameIts.CharmApp.dto.BookingDto;
import com.esameIts.CharmApp.entities.Booking;
import com.esameIts.CharmApp.entities.ServiceItem;

public final class BookingMapper {

    private BookingMapper() {}

    public static BookingDto toDto(Booking e) {
        if (e == null) return null;

        ServiceItem svc = e.getService();
        String serviceName = (svc != null) ? svc.getName() : null;
        Long serviceId = (svc != null) ? svc.getId() : null;
        String userName = (e.getUser() != null) ? e.getUser().getName() : null;
        
        return new BookingDto(
                e.getId(),
                serviceId,
                serviceName,
                userName,
                formatDate(e.getDate()),
                formatTime(e.getStartTime()),
                formatTime(e.getEndTime()),
                (e.getStatus() != null ? e.getStatus().name() : null),
                e.getNotes()
        );
    }

    public static List<BookingDto> toDtoList(List<Booking> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().filter(Objects::nonNull)
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    private static String formatDate(LocalDate d) {
        return d != null ? d.toString() : null; // ISO-8601 (YYYY-MM-DD)
        // In alternativa: DateTimeFormatter.ISO_LOCAL_DATE.format(d)
    }

    private static String formatTime(LocalTime t) {
        return t != null ? t.toString().substring(0,5) : null; // "HH:mm"
        // Oppure: t.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}
