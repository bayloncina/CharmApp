package com.esameIts.CharmApp.controllers;

import com.esameIts.CharmApp.dto.BookingDto;
import com.esameIts.CharmApp.dto.CreateBookingRequest;
import com.esameIts.CharmApp.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    // Crea prenotazione
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(Authentication auth, @Valid @RequestBody CreateBookingRequest in) {
        return bookingService.create(auth.getName(), in); // auth.getName() = email dal JWT
    }

    // Le mie prenotazioni
    @GetMapping("/me")
    public List<BookingDto> myBookings(Authentication auth) {
        return bookingService.getMyBookings(auth.getName());
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingDto> getAllBookings() {
        return bookingService.findAll();
    }

 // Cliente cancella la propria prenotazione
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyBooking(@PathVariable Long id, Authentication auth) {
        bookingService.deleteMyBooking(id, auth.getName());
    }

    // Admin cancella qualsiasi prenotazione
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByAdmin(@PathVariable Long id) {
        bookingService.deleteByAdmin(id);
    }


}