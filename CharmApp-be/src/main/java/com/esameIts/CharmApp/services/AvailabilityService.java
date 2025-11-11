package com.esameIts.CharmApp.services;

import com.esameIts.CharmApp.dto.OpenDayDto;
import com.esameIts.CharmApp.dto.SlotDto;
import com.esameIts.CharmApp.entities.*;
import com.esameIts.CharmApp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvailabilityService {

    private final ServiceItemRepository serviceRepo;
    private final WorkingHourRepository workingHourRepo;
    private final ClosureRepository closureRepo;
    private final BookingRepository bookingRepo;

    private static final int STEP_MIN = 15; // griglia slot

    public List<SlotDto> getDailySlots(Long serviceId, LocalDate date) {
        ServiceItem svc = serviceRepo.findById(serviceId)
                .orElse(null);
        if (svc == null) return List.of();

        // Orari base del giorno
        var whOpt = workingHourRepo.findByDay(date.getDayOfWeek());
        if (whOpt.isEmpty()) {
            // giorno non configurato => chiuso
            return List.of();
        }
        LocalTime open  = whOpt.get().getOpen();
        LocalTime close = whOpt.get().getClose();

        // Chiusure/override
        var cloOpt = closureRepo.findByDate(date);
        if (cloOpt.isPresent()) {
            Closure c = cloOpt.get();
            if (c.getOpenOverride() == null && c.getCloseOverride() == null) {
                return List.of(); // chiuso tutto il giorno
            }
            if (c.getOpenOverride() != null)  open  = c.getOpenOverride();
            if (c.getCloseOverride() != null) close = c.getCloseOverride();
        }

        // se open == close -> nessuno slot
        if (!open.isBefore(close)) return List.of();

        // Prenotazioni esistenti del giorno
        List<Booking> bookings = bookingRepo.findActiveByDate(date);

        int serviceTotal = svc.getDurationMin() + svc.getBufferMin();
        List<SlotDto> result = new ArrayList<>();

        for (LocalTime start = open; start.plusMinutes(serviceTotal).compareTo(close) <= 0; start = start.plusMinutes(STEP_MIN)) {
            LocalTime end = start.plusMinutes(serviceTotal);
            if (!overlapsAny(start, end, bookings)) {
                result.add(new SlotDto(fmt(start), fmt(end)));
            }
        }
        return result;
    }

   

    public List<OpenDayDto> getOpenDays(int year, int month, Long serviceId) {
        List<OpenDayDto> out = new ArrayList<>();

        LocalDate first = LocalDate.of(year, month, 1);
        LocalDate last  = first.withDayOfMonth(first.lengthOfMonth());

        for (LocalDate d = first; !d.isAfter(last); d = d.plusDays(1)) {
            var whOpt = workingHourRepo.findByDay(d.getDayOfWeek());
            if (whOpt.isEmpty()) continue; // giorno non configurato = chiuso

            LocalTime open  = whOpt.get().getOpen();
            LocalTime close = whOpt.get().getClose();

            var cloOpt = closureRepo.findByDate(d);
            if (cloOpt.isPresent()) {
                var c = cloOpt.get();
                if (c.getOpenOverride() == null && c.getCloseOverride() == null) {
                    continue; // chiuso tutto il giorno
                }
                if (c.getOpenOverride() != null)  open  = c.getOpenOverride();
                if (c.getCloseOverride() != null) close = c.getCloseOverride();
            }
            if (!open.isBefore(close)) continue; // open==close -> di fatto chiuso

            // Se non mi interessa il servizio/slot, il giorno è buono
            if (serviceId == null) {
                out.add(new OpenDayDto(d.toString(), fmt(open), fmt(close)));
                continue;
            }

            // Altrimenti, verifica che ci sia almeno UNO slot per quel servizio
            ServiceItem svc = serviceRepo.findById(serviceId).orElse(null);
            if (svc == null) continue;

            int totalMin = svc.getDurationMin() + svc.getBufferMin();
            var bookings = bookingRepo.findActiveByDate(d);

            boolean hasAny = false;
            for (LocalTime start = open; start.plusMinutes(totalMin).compareTo(close) <= 0; start = start.plusMinutes(STEP_MIN)) {
                LocalTime end = start.plusMinutes(totalMin);
                if (!overlapsAny(start, end, bookings)) { hasAny = true; break; }
            }
            if (hasAny) out.add(new OpenDayDto(d.toString(), fmt(open), fmt(close)));
        }
        return out;
    }
    
    private boolean overlapsAny(LocalTime start, LocalTime end, List<Booking> bookings) {
        for (Booking b : bookings) {
            if (start.isBefore(b.getEndTime()) && b.getStartTime().isBefore(end)) {
                return true;
            }
        }
        return false;
    }

    private String fmt(LocalTime t) { return t.toString().substring(0,5); }
}

