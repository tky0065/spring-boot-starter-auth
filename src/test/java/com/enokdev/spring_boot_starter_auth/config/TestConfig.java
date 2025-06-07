package com.enokdev.spring_boot_starter_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.mockito.Mockito;
import org.thymeleaf.ITemplateEngine;

/**
 * Configuration pour l'environnement de test
 * Fournit des implémentations simulées des dépendances externes
 */
@Configuration
@Profile("test")
public class TestConfig {

    /**
     * Crée un JavaMailSender factice pour les tests
     * Évite la nécessité de se connecter à un serveur SMTP réel
     */
    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        // Simple implémentation par défaut qui ne fait rien en réalité
        return new JavaMailSenderImpl();
    }

    /**
     * Au lieu de créer un nouveau TemplateEngine, nous laissons Spring Boot
     * utiliser son propre bean TemplateEngine automatiquement configuré
     */
}
