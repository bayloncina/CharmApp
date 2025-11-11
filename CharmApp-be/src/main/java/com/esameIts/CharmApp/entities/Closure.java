package com.esameIts.CharmApp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "closures", indexes = @Index(name = "idx_closures_date", columnList = "date"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id") @ToString

public class Closure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "open_override")
    private LocalTime openOverride;   // null = chiuso tutto il giorno

    @Column(name = "close_override")
    private LocalTime closeOverride;

    @Column(length = 200)
    private String reason;
}