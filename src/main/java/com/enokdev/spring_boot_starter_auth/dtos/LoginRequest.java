package com.enokdev.spring_boot_starter_auth.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}