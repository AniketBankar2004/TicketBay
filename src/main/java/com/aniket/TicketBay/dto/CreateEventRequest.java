package com.aniket.TicketBay.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateEventRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "venue is required")
    private String venue;

    @NotNull(message = "Event Date is required")
    @Future(message = "Event date must be in future")
    private LocalDateTime eventDate;

    @NotNull(message = "Total seats is required")
    @Min(value =1,message="Must have at least 1 seat")
    private Integer totalSeats;

    @NotNull(message="Price is required")
    @DecimalMin(value = "0.0",message="Price cannot be negative")
    private BigDecimal price;
}
