package com.enokdev.spring_boot_starter_auth.repositories;

import com.enokdev.spring_boot_starter_auth.entities.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findByResetToken(String token);

    Optional<AuthUser> findByEmailConfirmationToken(String token);

    Optional<AuthUser> findByUsernameOrEmail(String username, String email);
}