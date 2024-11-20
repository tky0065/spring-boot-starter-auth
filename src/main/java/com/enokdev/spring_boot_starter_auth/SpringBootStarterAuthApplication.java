package com.enokdev.spring_boot_starter_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringBootStarterAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStarterAuthApplication.class, args);
	}

}
