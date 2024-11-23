package com.enokdev.spring_boot_starter_auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles = new HashSet<>();
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean accountNonLocked;
    private String message;
    private LocalDateTime lastLogin;
}
