package com.enokdev.spring_boot_starter_auth.config;
//
//import org.springframework.boot.autoconfigure.AutoConfiguration;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//@AutoConfiguration
//@EnableWebSecurity
//@ComponentScan(basePackages = {
//        "com.enokdev.spring_boot_starter_auth.controllers",
//        "com.enokdev.spring_boot_starter_auth.services",
//        "com.enokdev.spring_boot_starter_auth.config",
//        "com.enokdev.spring_boot_starter_auth.repositories"
//})
//@EntityScan(basePackages = "com.enokdev.spring_boot_starter_auth.entities")
//@EnableJpaRepositories(basePackages = "com.enokdev.spring_boot_starter_auth.repositories")
//@EnableConfigurationProperties(AuthProperties.class)
//public class AuthAutoConfiguration {
//
//    @Bean
//    @ConditionalOnMissingBean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}



import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.enokdev.spring_boot_starter_auth")
public class AuthAutoConfiguration {
}
