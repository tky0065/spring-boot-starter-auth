package com.enokdev.spring_boot_starter_auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring-boot-starter-auth.oauth2")
public class OAuth2Properties {
    /**
     * Activer/désactiver l'authentification OAuth2
     */
    private boolean enabled = false;

    /**
     * URL de redirection après connexion réussie
     */
    private String successUrl = "/";

    /**
     * URL de redirection en cas d'échec d'authentification
     */
    private String failureUrl = "/login?error=true";

    /**
     * Liste des URI de redirection autorisées pour OAuth2
     */
    private List<String> authorizedRedirectUris = new ArrayList<>();
}
