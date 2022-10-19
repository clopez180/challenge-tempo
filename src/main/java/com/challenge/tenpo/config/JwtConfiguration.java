package com.challenge.tenpo.config;

import com.challenge.tenpo.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Value("${jwt.ttl:60}")
    private int jwtTtl;

    @Bean
    public JwtGenerator createJwtGenerator() {
        return new JwtGenerator(jwtTtl);
    }
}