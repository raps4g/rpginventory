package com.raps4g.rpginventory.config;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties(prefix = "app")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private Boolean loadItems = true;
    private String adminPassword;

    public void ensureAdminPassword() {
        if (adminPassword == null || adminPassword.isEmpty()) {
            adminPassword = UUID.randomUUID().toString();
            logger.warn("adminPassword is not set. A random password has been generated.");
            logger.warn("Generated adminPassword: {}", adminPassword);
        }}
}
