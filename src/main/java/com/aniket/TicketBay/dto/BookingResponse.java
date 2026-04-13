package com.aniket.TicketBay.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private String bookingRef;

    private String eventTitle;

    private int seatsBooked;

    private String status;
}
