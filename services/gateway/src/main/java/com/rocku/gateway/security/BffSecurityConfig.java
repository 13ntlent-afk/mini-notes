package com.rocku.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

@Configuration
public class BffSecurityConfig {
    @Bean
    SecurityWebFilterChain bffSecurityFilterChain(ServerHttpSecurity http,
            @Value("${SESSION_COOKIE_SECURE:false}") boolean secureCookies) {
        var csrfRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
        csrfRepository.setCookieCustomizer(cookie -> cookie.path("/").sameSite("Lax").secure(secureCookies));

        return http
                .csrf(csrf -> csrf.csrfTokenRepository(csrfRepository))
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                .build();
    }
}