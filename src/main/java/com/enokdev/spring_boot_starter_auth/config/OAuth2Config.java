package com.enokdev.spring_boot_starter_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties
public class OAuth2Config {
    @Bean
    public OAuth2Properties oAuth2Properties() {
        return new OAuth2Properties();
    }
}