package com.enokdev.spring_boot_starter_auth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    @Column(nullable = false)
    private String password;


    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String firstName;
    @Column(nullable = true)
    private String lastName;
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
}
