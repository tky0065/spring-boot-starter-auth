package com.enokdev.spring_boot_starter_auth.repositories;

import com.enokdev.spring_boot_starter_auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String token);

    Optional<User> findByEmailConfirmationToken(String token);

    Optional<User> findByUsernameOrEmail(String username, String email);
}