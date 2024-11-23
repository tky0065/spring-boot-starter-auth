package com.enokdev.spring_boot_starter_auth.exeption;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}