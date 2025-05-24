package com.enokdev.spring_boot_starter_auth;

import com.enokdev.spring_boot_starter_auth.config.AuthProperties;
import com.enokdev.spring_boot_starter_auth.config.OAuth2Properties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties({AuthProperties.class, OAuth2Properties.class})

@ActiveProfiles("test")
class SpringBootStarterAuthApplicationTests {

	@Test
	void contextLoads() {
		// Le test vérifie simplement que le contexte Spring se charge correctement
	}

}
