package com.enokdev.spring_boot_starter_auth.exeption;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}