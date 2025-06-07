package com.enokdev.spring_boot_starter_auth.api;

import com.enokdev.spring_boot_starter_auth.dtos.AuthResponse;
import com.enokdev.spring_boot_starter_auth.dtos.LoginRequest;
import com.enokdev.spring_boot_starter_auth.dtos.RegisterRequest;
import com.enokdev.spring_boot_starter_auth.dtos.AuthUserDTO;
import com.enokdev.spring_boot_starter_auth.dtos.UserProfileUpdateDto;
import com.enokdev.spring_boot_starter_auth.entities.LoginHistory;
import com.enokdev.spring_boot_starter_auth.entities.AuthUser;

import java.util.List;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthUserDTO getCurrentUser();
    void logout();

    void  confirmEmail(String token);

    void initiatePasswordReset(String email);

    void resetPassword(String token, String newPassword);
    AuthUser updateProfile(Long userId, UserProfileUpdateDto updateDto);
    void recordLoginAttempt(String email, boolean success);
    List<LoginHistory> getLoginHistory(Long userId);


    void unlockWhenTimeExpired(AuthUser user);
}
