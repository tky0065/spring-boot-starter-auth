package com.enokdev.spring_boot_starter_auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdateDto {
    
    private String username;

    private String email;

    private String password;

    private String firstName;


    private String lastName;
}
