package com.aniket.TicketBay.dto;

import com.aniket.TicketBay.model.AppUser;
import com.aniket.TicketBay.model.Event;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EventResponse {
    private UUID id;

    private String title;

    private String description;

    private String venue;

    private LocalDateTime eventDate;

    private int totalSeats;

    private int availableSeats;

    private BigDecimal price;

    private String createdBy;

    private String status;

    public static EventResponse toResponse(Event event){
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .venue(event.getVenue())
                .eventDate(event.getEventDate())
                .totalSeats(event.getTotalSeats())
                .availableSeats(event.getAvailableSeats())
                .price(event.getPrice())
                .status(event.getStatus().name())
                .createdBy(event.getCreatedBy().getName())
                .build();
    }

}
