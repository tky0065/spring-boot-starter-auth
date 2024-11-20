package com.enokdev.spring_boot_starter_auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final AuthProperties authProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(authProperties.getSwagger().getTitle())
                        .version(authProperties.getSwagger().getVersion())
                        .description(authProperties.getSwagger().getDescription()));
    }
}