package com.enokdev.spring_boot_starter_auth.controllers;


import com.enokdev.spring_boot_starter_auth.dtos.AuthResponse;
import com.enokdev.spring_boot_starter_auth.dtos.LoginRequest;
import com.enokdev.spring_boot_starter_auth.dtos.RegisterRequest;
import com.enokdev.spring_boot_starter_auth.dtos.UserDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthController {

    ResponseEntity<AuthResponse> register(RegisterRequest request);

    ResponseEntity<AuthResponse> login(LoginRequest request);

    ResponseEntity<UserDTO> getCurrentUser();

    ResponseEntity<Void> logout();
}
