package com.enokdev.spring_boot_starter_auth.api;

import com.enokdev.spring_boot_starter_auth.dtos.AuthResponse;
import com.enokdev.spring_boot_starter_auth.dtos.LoginRequest;
import com.enokdev.spring_boot_starter_auth.dtos.RegisterRequest;
import com.enokdev.spring_boot_starter_auth.dtos.UserDTO;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserDTO getCurrentUser();
    void logout();
}
