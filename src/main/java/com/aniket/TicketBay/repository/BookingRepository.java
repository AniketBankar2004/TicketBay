package com.aniket.TicketBay.repository;

import com.aniket.TicketBay.model.AppUser;
import com.aniket.TicketBay.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking,UUID> {

    List<Booking> findByUser(AppUser user);

    Optional<Booking> findByBookingRef(String id);
}
