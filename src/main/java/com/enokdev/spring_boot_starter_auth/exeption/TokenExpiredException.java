package com.enokdev.spring_boot_starter_auth.exeption;

import lombok.Getter;

import java.time.LocalDateTime;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
