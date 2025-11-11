package com.esameIts.CharmApp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import com.esameIts.CharmApp.ENUM.BookingStatus;

@Entity
@Table(name = "bookings",
       indexes = {
         @Index(name = "idx_bookings_user", columnList = "user_id"),
         @Index(name = "idx_bookings_date", columnList = "date,start_time")
       })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id") @ToString(exclude = {"user","service"})
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_booking_user"))
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", foreignKey = @ForeignKey(name = "fk_booking_service"))
    private ServiceItem service;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    @Builder.Default
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}