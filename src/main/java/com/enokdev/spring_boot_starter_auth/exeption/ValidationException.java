package com.enokdev.spring_boot_starter_auth.exeption;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}