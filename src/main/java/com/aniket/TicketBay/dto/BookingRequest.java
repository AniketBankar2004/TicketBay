package com.aniket.TicketBay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookingRequest {

    private UUID eventId;

    private int numSeats;

}
