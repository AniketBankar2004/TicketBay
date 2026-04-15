package com.aniket.TicketBay.controller;

import com.aniket.TicketBay.dto.BookingRequest;
import com.aniket.TicketBay.dto.BookingResponse;
import com.aniket.TicketBay.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;


    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request, Authentication authentication){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookTicket(request,authentication));
    }

    @DeleteMapping("/{bookingRef}")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable String bookingRef, Authentication authentication) {
        return ResponseEntity.ok(bookingService.cancelBooking(authentication, bookingRef));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getMyEvents(authentication));
    }

}
