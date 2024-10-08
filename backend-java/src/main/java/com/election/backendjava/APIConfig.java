package com.election.backendjava;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class APIConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("https://localhost:8080", "http://localhost:3001", "http://*.hva.nl:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
