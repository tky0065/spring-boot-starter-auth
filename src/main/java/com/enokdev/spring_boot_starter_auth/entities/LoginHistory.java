package com.enokdev.spring_boot_starter_auth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    private String location; // Pays/Ville basé sur l'IP

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "session_id")
    private String sessionId;

    // Méthodes utilitaires
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public static LoginHistory createSuccessLogin(User user, String ipAddress, String userAgent) {
        return LoginHistory.builder()
                .user(user)
                .success(true)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();
    }

    public static LoginHistory createFailedLogin(User user, String ipAddress, String userAgent, String failureReason) {
        return LoginHistory.builder()
                .user(user)
                .success(false)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .failureReason(failureReason)
                .build();
    }
}
