package com.enokdev.spring_boot_starter_auth.exeption;

public class PasswordValidationException extends RuntimeException {
    public PasswordValidationException(String message) {
        super(message);
    }
}