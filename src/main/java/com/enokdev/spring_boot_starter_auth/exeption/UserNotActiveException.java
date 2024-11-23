package com.enokdev.spring_boot_starter_auth.exeption;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String message) {
        super(message);
    }
}
