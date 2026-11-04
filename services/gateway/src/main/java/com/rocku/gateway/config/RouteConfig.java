package com.rocku.gateway.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Bean
    RouteLocator routes(RouteLocatorBuilder builder,
            @Value("${AUTH_SERVICE_URL}") String authUrl,
            @Value("${NOTES_SERVICE_URL}") String notesUrl,
            @Value("${CONTACT_SERVICE_URL}") String contactUrl) {
        return builder.routes()
                .route("auth", route -> route.path("/api/auth/**")
                        .filters(filters -> filters.stripPrefix(1)
                                .circuitBreaker(config -> config.setName("authCircuit")
                                        .setFallbackUri("forward:/fallback/auth")))
                        .metadata("response-timeout", Duration.ofSeconds(3))
                        .uri(authUrl))
                .route("notes", route -> route.path("/api/notes", "/api/notes/**")
                        .filters(filters -> filters.stripPrefix(1)
                                .circuitBreaker(config -> config.setName("notesCircuit")
                                        .setFallbackUri("forward:/fallback/notes")))
                        .metadata("response-timeout", Duration.ofSeconds(4))
                        .uri(notesUrl))
                .route("print", route -> route.path("/print/**")
                        .filters(filters -> filters.circuitBreaker(config -> config.setName("printCircuit")
                                .setFallbackUri("forward:/fallback/notes")))
                        .metadata("response-timeout", Duration.ofSeconds(5))
                        .uri(notesUrl))
                .route("contact", route -> route.path("/api/contact", "/api/contact/**")
                        .filters(filters -> filters.stripPrefix(1)
                                .circuitBreaker(config -> config.setName("contactCircuit")
                                        .setFallbackUri("forward:/fallback/contact")))
                        .metadata("response-timeout", Duration.ofSeconds(3))
                        .uri(contactUrl))
                .route("admin", route -> route.path("/api/admin/**")
                        .filters(filters -> filters.stripPrefix(1)
                                .circuitBreaker(config -> config.setName("adminCircuit")
                                        .setFallbackUri("forward:/fallback/contact")))
                        .metadata("response-timeout", Duration.ofSeconds(3))
                        .uri(contactUrl))
                .build();
    }
}