package com.aniket.TicketBay.repository;

import com.aniket.TicketBay.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByProviderAndProviderId(String provider, String providerId);
}
