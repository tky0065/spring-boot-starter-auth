package com.enokdev.spring_boot_starter_auth;

import com.enokdev.spring_boot_starter_auth.config.AuthProperties;
import com.enokdev.spring_boot_starter_auth.config.OAuth2Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AuthProperties.class, OAuth2Properties.class})
public class SpringBootStarterAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStarterAuthApplication.class, args);
	}

}
