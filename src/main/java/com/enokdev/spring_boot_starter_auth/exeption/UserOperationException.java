package com.enokdev.spring_boot_starter_auth.exeption;

public class UserOperationException extends RuntimeException {
    public UserOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}