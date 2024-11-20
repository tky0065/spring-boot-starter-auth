package com.enokdev.spring_boot_starter_auth.config;



import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@AutoConfiguration
@EnableWebSecurity
@ComponentScan(basePackages = {
        "com.enokdev.spring_boot_starter_auth.controllers",
        "com.enokdev.spring_boot_starter_auth.services",
        "com.enokdev.spring_boot_starter_auth.config"
})
@EntityScan("com.enokdev.spring_boot_starter_auth.entities")
@EnableJpaRepositories("com.enokdev.spring_boot_starter_auth.repositories")
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration {
    // La configuration sera chargée automatiquement
}