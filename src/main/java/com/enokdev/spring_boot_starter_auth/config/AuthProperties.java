package com.enokdev.spring_boot_starter_auth.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private final Jwt jwt = new Jwt();
    private final Swagger swagger = new Swagger();

    @Data
    public static class Jwt {
        private String secret = "defaultSecretKey404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        private long expiration = 86400000; // 24 hours
    }

    @Data
    public static class Swagger {
        private String title = "Authentication API";
        private String description = "API for authentication management";
        private String version = "1.0";
    }
}