package com.enokdev.spring_boot_starter_auth.controllers;

import com.enokdev.spring_boot_starter_auth.dtos.AuthResponse;
import com.enokdev.spring_boot_starter_auth.dtos.ErrorResponse;
import com.enokdev.spring_boot_starter_auth.dtos.LoginHistoryResponse;
import com.enokdev.spring_boot_starter_auth.dtos.LoginRequest;
import com.enokdev.spring_boot_starter_auth.dtos.RegisterRequest;
import com.enokdev.spring_boot_starter_auth.dtos.UserDTO;
import com.enokdev.spring_boot_starter_auth.api.IAuthService;
import com.enokdev.spring_boot_starter_auth.dtos.UserProfileUpdateDto;
import com.enokdev.spring_boot_starter_auth.dtos.UserResponse;
import com.enokdev.spring_boot_starter_auth.entities.LoginHistory;
import com.enokdev.spring_boot_starter_auth.entities.User;
import com.enokdev.spring_boot_starter_auth.exeption.AccountLockedException;
import com.enokdev.spring_boot_starter_auth.exeption.UserNotActiveException;
import com.enokdev.spring_boot_starter_auth.exeption.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Endpoints d'authentification")
public class AuthController {

    private final IAuthService authService;


    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur")
    public AuthResponse register(@Valid @RequestBody RegisterRequest registrationDto) {
        AuthResponse user = authService.register(registrationDto);
        return AuthResponse.builder()
                .token(user.getToken())
                .type("Bearer")
                .username(user.getUsername())
                .roles(user.getRoles())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.debug("Processing login request for username: {}", loginRequest.getUsername());
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            log.warn("Login failed: Invalid credentials for user: {}", loginRequest.getUsername());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password"));
        } catch (DisabledException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Account is not activated. Please check your email."));
        } catch (LockedException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Account is locked. Please contact support."));
        } catch (Exception e) {
            log.error("Login error", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred during login"));
        }
    }




    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            UserDTO currentUser = authService.getCurrentUser();
            return ResponseEntity.ok(currentUser);
        } catch (UserNotFoundException e) {
            log.warn("Attempt to get current user with invalid session");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("User session is invalid or expired"));
        } catch (Exception e) {
            log.error("Error retrieving current user", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error retrieving user information"));
        }
    }



    @PostMapping("/logout")
    @Operation(summary = "Déconnexion")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam String token) {
        authService.confirmEmail(token);
        return ResponseEntity.ok("Email confirmed successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam @Valid @Size(min = 6) String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PutMapping("/{userId}/profile")
    @PreAuthorize("@userSecurity.hasUserId(#userId)")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileUpdateDto updateDto) {
        User updatedUser = authService.updateProfile(userId, updateDto);
        return ResponseEntity.ok(UserResponse.fromUser(updatedUser));
    }

    @GetMapping("/{userId}/login-history")
    @PreAuthorize("@userSecurity.hasUserId(#userId)")
    public ResponseEntity<List<LoginHistoryResponse>> getLoginHistory(@PathVariable Long userId) {
        List<LoginHistory> history = authService.getLoginHistory(userId);
        return ResponseEntity.ok(LoginHistoryResponse.fromLoginHistory(history));
    }
}
