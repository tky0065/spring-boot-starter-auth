package com.enokdev.spring_boot_starter_auth.dtos;

import com.enokdev.spring_boot_starter_auth.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private Long id;
    private String token;
    private String type = "Bearer";
    private String email;
    private String firstName;
    private String lastName;
    private String username;

    private Set<String> roles;
    private String message;

    public static Object fromUser(User updatedUser) {
        return null;
    }
}