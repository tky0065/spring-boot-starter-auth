package com.enokdev.spring_boot_starter_auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration automatique pour le starter d'authentification.
 * Cette classe active les propriétés de configuration et effectue le scan des composants nécessaires.
 */
@Configuration
@ComponentScan(basePackages = "com.enokdev.spring_boot_starter_auth")
@EnableConfigurationProperties({AuthProperties.class, OAuth2Properties.class})
public class AuthAutoConfiguration {
    // La configuration est automatiquement chargée par Spring Boot
}
