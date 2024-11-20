package com.enokdev.spring_boot_starter_auth.dtos;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
}
