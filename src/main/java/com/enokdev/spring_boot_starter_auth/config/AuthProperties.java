package com.enokdev.spring_boot_starter_auth.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration des propriétés d'authentification pour Spring Boot Starter Auth
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    /**
     * Configuration JWT pour l'authentification
     */
    private final Jwt jwt = new Jwt();

    /**
     * Configuration Swagger pour la documentation API
     */
    private final Swagger swagger = new Swagger();

    /**
     * Configuration de l'authentification par JWT
     */
    @Data
    public static class Jwt {
        /**
         * Clé secrète pour la signature des tokens JWT
         * Doit être suffisamment longue et complexe pour garantir la sécurité
         */
        private String secret = "defaultSecretKey404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

        /**
         * Durée de validité du token JWT en millisecondes
         * Par défaut: 24 heures (86400000 ms)
         */
        private long expiration = 86400000;
    }

    /**
     * Configuration de Swagger UI pour la documentation de l'API
     */
    @Data
    public static class Swagger {
        /**
         * Titre de la documentation Swagger
         */
        private String title = "Authentication API";

        /**
         * Description de la documentation Swagger
         */
        private String description = "API for authentication management";

        /**
         * Version de l'API documentée par Swagger
         */
        private String version = "1.0";
    }
}

