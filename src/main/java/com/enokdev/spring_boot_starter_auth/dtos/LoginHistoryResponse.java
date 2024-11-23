package com.enokdev.spring_boot_starter_auth.dtos;

import com.enokdev.spring_boot_starter_auth.entities.LoginHistory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class LoginHistoryResponse {
    private LocalDateTime timestamp;
    private boolean success;
    private String ipAddress;
    private String userAgent;
    private String location; // Pays/Ville basé sur l'IP

    public static List<LoginHistoryResponse> fromLoginHistory(List<LoginHistory> history) {
        return history.stream()
                .map(h -> LoginHistoryResponse.builder()
                        .timestamp(h.getTimestamp())
                        .success(h.isSuccess())
                        .ipAddress(h.getIpAddress())
                        .userAgent(h.getUserAgent())
                        .location(h.getLocation())
                        .build())
                .collect(Collectors.toList());
    }
}
