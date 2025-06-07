package com.enokdev.spring_boot_starter_auth.entities;

import com.enokdev.spring_boot_starter_auth.oauth2.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_users")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = true)  // Rendre nullable pour OAuth2
    private String password;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String name;     // Ajout pour OAuth2

    @Column(nullable = true)
    private String imageUrl; // Ajout pour OAuth2

    private boolean emailVerified = false; // Ajout pour OAuth2

    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.LOCAL; // Ajout pour OAuth2

    private String providerId; // Ajout pour OAuth2

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private boolean enabled = false;
    private boolean accountNonLocked = true;

    private int failedAttempts = 0;
    private LocalDateTime lockTime;

    @Column(nullable = true)
    private String resetToken;
    private LocalDateTime resetTokenExpiry;

    @Column(nullable = true)
    private String emailConfirmationToken;

    @OneToMany(mappedBy = "user")
    private List<LoginHistory> loginHistory = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    public boolean isLocked() {
        return !this.isAccountNonLocked();
    }
}
