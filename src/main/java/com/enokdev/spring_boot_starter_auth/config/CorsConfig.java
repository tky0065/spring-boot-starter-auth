package com.enokdev.spring_boot_starter_auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration CORS pour permettre les requêtes cross-origin
 * vers les endpoints d'authentification.
 */
@Configuration
public class CorsConfig {

    @Value("${spring-boot-starter-auth.cors.allowed-origins:*}")
    private List<String> allowedOrigins;

    @Value("${spring-boot-starter-auth.cors.allowed-methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}")
    private List<String> allowedMethods;

    @Value("${spring-boot-starter-auth.cors.allowed-headers:Authorization,Content-Type,X-Requested-With,Accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers}")
    private List<String> allowedHeaders;

    @Value("${spring-boot-starter-auth.cors.exposed-headers:Authorization}")
    private List<String> exposedHeaders;

    @Value("${spring-boot-starter-auth.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${spring-boot-starter-auth.cors.max-age:3600}")
    private long maxAge;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Configurer les origines autorisées
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            if (allowedOrigins.contains("*")) {
                configuration.addAllowedOriginPattern("*");
            } else {
                configuration.setAllowedOrigins(allowedOrigins);
            }
        }

        // Configurer les méthodes autorisées
        configuration.setAllowedMethods(allowedMethods);

        // Configurer les en-têtes autorisés
        configuration.setAllowedHeaders(allowedHeaders);

        // Configurer les en-têtes exposés
        configuration.setExposedHeaders(exposedHeaders);

        // Autoriser les cookies et l'authentification
        configuration.setAllowCredentials(allowCredentials);

        // Durée de mise en cache des résultats pre-flight
        configuration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        source.registerCorsConfiguration("/oauth2/**", configuration);

        return new CorsFilter(source);
    }
}
