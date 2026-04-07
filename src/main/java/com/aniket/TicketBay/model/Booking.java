package com.aniket.TicketBay.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "booking_ref", nullable = false, unique = true, updatable = false)
    private String bookingRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id",nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private Status status = Status.CONFIRMED;

    @Column(name = "seats_booked", nullable = false)
    @Builder.Default
    private int seatsBooked = 1;

    @CreationTimestamp
    @Column(name = "booked_at", updatable = false, nullable = false)
    private Instant bookedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    public enum Status {
        CONFIRMED,
        CANCELLED
    }
}




