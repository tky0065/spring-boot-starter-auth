package com.enokdev.spring_boot_starter_auth.config;

import com.enokdev.spring_boot_starter_auth.repositories.UserRepository;
import com.enokdev.spring_boot_starter_auth.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class UserDetailsServiceConfig {

    private final UserRepository userRepository;

    public UserDetailsServiceConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }
}