package com.esameIts.CharmApp.controllers;

import com.esameIts.CharmApp.dto.OpenDayDto;
import com.esameIts.CharmApp.dto.SlotDto;
import com.esameIts.CharmApp.services.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    // Esempio: GET /availability?serviceId=1&date=2025-08-25
    @GetMapping("/availability")
    public List<SlotDto> availability(
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return availabilityService.getDailySlots(serviceId, date);
    }

    // Esempi:
    // GET /availability/open-days?year=2025&month=8
    // GET /availability/open-days?year=2025&month=8&serviceId=1
    @GetMapping("/availability/open-days")
    public List<OpenDayDto> openDays(@RequestParam int year,
                                     @RequestParam int month,
                                     @RequestParam(required = false) Long serviceId) {
        return availabilityService.getOpenDays(year, month, serviceId);
    }
}
