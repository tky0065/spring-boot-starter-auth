package com.enokdev.spring_boot_starter_auth.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotEmpty
    @Min(6)
    private String username;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Min(6)
    private String password;
}
