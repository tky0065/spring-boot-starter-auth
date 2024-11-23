package com.enokdev.spring_boot_starter_auth.api;

public interface IEmailService {
    void sendConfirmationEmail(String to, String token);
    void sendPasswordResetEmail(String to, String token);
    void sendAccountLockedEmail(String to);

}
