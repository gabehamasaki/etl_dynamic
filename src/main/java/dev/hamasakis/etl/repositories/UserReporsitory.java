package dev.hamasakis.etl.repositories;

import dev.hamasakis.etl.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserReporsitory extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
