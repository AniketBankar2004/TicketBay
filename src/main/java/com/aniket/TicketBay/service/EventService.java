package com.aniket.TicketBay.service;

import com.aniket.TicketBay.dto.CreateEventRequest;
import com.aniket.TicketBay.dto.EventResponse;
import com.aniket.TicketBay.dto.UpdateEventRequest;
import com.aniket.TicketBay.exception.UserNotFoundException;
import com.aniket.TicketBay.model.AppUser;
import com.aniket.TicketBay.model.Event;
import com.aniket.TicketBay.repository.EventRepository;
import com.aniket.TicketBay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public EventResponse createEvent(CreateEventRequest request, Authentication authentication){
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");

        AppUser creator = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("user not found"));

        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .venue(request.getVenue())
                .eventDate(request.getEventDate())
                .totalSeats(request.getTotalSeats())
                .availableSeats(request.getTotalSeats())  // starts fully available
                .price(request.getPrice())
                .status(Event.Status.ACTIVE)
                .createdBy(creator)
                .build();

        Event saved = eventRepository.save(event);
        return EventResponse.toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public EventResponse updateEvent(UUID id, UpdateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));


        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getVenue() != null) {
            event.setVenue(request.getVenue());
        }
        if (request.getEventDate() != null) {
            if (request.getEventDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Event date cannot be in the past");
            }
            event.setEventDate(request.getEventDate());
        }
        if (request.getPrice() != null) {
            event.setPrice(request.getPrice());
        }
        if (request.getStatus() != null) {
            event.setStatus(Event.Status.valueOf(request.getStatus().toUpperCase()));
        }


        if (request.getTotalSeats() != null) {
            int alreadyBooked = event.getTotalSeats() - event.getAvailableSeats();
            if (request.getTotalSeats() < alreadyBooked) {
                throw new RuntimeException(
                        "Cannot reduce total seats to " + request.getTotalSeats()
                                + " — " + alreadyBooked + " seats already booked"
                );
            }
            int newAvailable = request.getTotalSeats() - alreadyBooked;
            event.setTotalSeats(request.getTotalSeats());
            event.setAvailableSeats(newAvailable);
        }

        Event saved = eventRepository.save(event);
        return EventResponse.toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteEvent(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        eventRepository.deleteById(event.getId());

    }


    public EventResponse getEventById(UUID id) {
        var event =  eventRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Event not found"));



        return EventResponse.toResponse(event);
    }

    public List<EventResponse> getEvents() {
        var events = eventRepository.findAll();
        return events.stream()
                .filter(event -> LocalDateTime.now().isBefore(event.getEventDate()) && event.getStatus().equals(Event.Status.ACTIVE) )
                .map(EventResponse::toResponse)
                .toList();
    }
}
