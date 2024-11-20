package com.enokdev.spring_boot_starter_auth.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty
    @Min(3)
    private String username;
    @NotEmpty
    @Min(6)
    private String password;
}