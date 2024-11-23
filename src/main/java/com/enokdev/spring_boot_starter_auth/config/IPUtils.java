package com.enokdev.spring_boot_starter_auth.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class IPUtils {

    @Autowired
    private HttpServletRequest request;

    public String getCurrentIpAddress() {
        String[] HEADERS_TO_TRY = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        String ip = null;
        for (String header : HEADERS_TO_TRY) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                break;
            }
        }

        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // Gérer les IP multiples (X-Forwarded-For peut contenir plusieurs IP)
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        log.debug("Detected IP address: {}", ip);
        return ip;
    }

    private boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    public String getCurrentUserAgent() {
        return Optional.ofNullable(request.getHeader("User-Agent"))
                .filter(ua -> !ua.isEmpty())
                .orElse("Unknown");
    }

}