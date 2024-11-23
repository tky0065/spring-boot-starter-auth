package com.enokdev.spring_boot_starter_auth.exeption;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}