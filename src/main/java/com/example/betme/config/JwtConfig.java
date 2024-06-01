package com.example.betme.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.signing.key}")
    private String secretKey;

    @Value("")
    private String duration;

    public String getSecretKey() {
        return secretKey;
    }
    public String getDuration() {
        return duration;
    }
}
