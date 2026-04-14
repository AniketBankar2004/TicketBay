package com.aniket.TicketBay.service;

import com.aniket.TicketBay.dto.BookingRequest;
import com.aniket.TicketBay.dto.BookingResponse;
import com.aniket.TicketBay.dto.EventResponse;
import com.aniket.TicketBay.exception.AccessDeniedException;
import com.aniket.TicketBay.exception.UserNotFoundException;
import com.aniket.TicketBay.model.AppUser;
import com.aniket.TicketBay.model.Booking;
import com.aniket.TicketBay.model.Event;
import com.aniket.TicketBay.repository.BookingRepository;
import com.aniket.TicketBay.repository.EventRepository;
import com.aniket.TicketBay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponse bookTicket(BookingRequest request, Authentication authentication){
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("user not found"));

        var event = eventRepository.findById(request.getEventId())
                .orElseThrow(()->new RuntimeException("Event doesn't exist"));

        if(request.getNumSeats()>event.getAvailableSeats()){
            throw new RuntimeException("Insufficient available tickets"+"Available tickets: "+ event.getAvailableSeats());
        }

        event.setAvailableSeats(event.getAvailableSeats() - request.getNumSeats());
        eventRepository.save(event);

        Booking newBooking = Booking.builder()
                .bookedAt(Instant.now())
                .seatsBooked(request.getNumSeats())
                .event(event)
                .user(user)
                .status(Booking.Status.CONFIRMED)
                .bookedAt(Instant.now())
                .bookingRef(UUID.randomUUID().toString().substring(0,8).toUpperCase())
                .build();

        Booking savedBooking = bookingRepository.save(newBooking);
        return toBookingResponse(savedBooking);

    }
    @Transactional
    public BookingResponse cancelBooking(Authentication authentication, String bookingRef) {
        Booking booking = bookingRepository.findByBookingRef(bookingRef)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == Booking.Status.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to cancel this booking");
        }

        Event event = booking.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + booking.getSeatsBooked());
        eventRepository.save(event);

        booking.setStatus(Booking.Status.CANCELLED);
        Booking savedBooking = bookingRepository.save(booking);

        return toBookingResponse(savedBooking);
    }

    private BookingResponse toBookingResponse(Booking savedBooking) {
        return BookingResponse.builder()
                .eventTitle(savedBooking.getEvent().getTitle())
                .seatsBooked(savedBooking.getSeatsBooked())
                .status(savedBooking.getStatus().toString())
                .bookingRef(savedBooking.getBookingRef())
                .build();
    }

    public List<BookingResponse> getMyEvents(Authentication authentication) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return bookingRepository.findByUser(user)
                .stream()
                .map(this::toBookingResponse)
                .toList();
    }
}
