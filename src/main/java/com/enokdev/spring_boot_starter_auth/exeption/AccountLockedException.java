package com.enokdev.spring_boot_starter_auth.exeption;

import java.time.LocalDateTime;

public class AccountLockedException extends RuntimeException {
    private final LocalDateTime lockTime;
    private final LocalDateTime unlockTime;

    public AccountLockedException(String message) {
        super(message);
        this.lockTime = LocalDateTime.now();
        this.unlockTime = lockTime.plusHours(24);
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public LocalDateTime getUnlockTime() {
        return unlockTime;
    }
}