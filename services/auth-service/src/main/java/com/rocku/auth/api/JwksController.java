package com.rocku.auth.api;

import com.nimbusds.jose.jwk.JWKSet;
import com.rocku.auth.domain.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/.well-known")
public class JwksController {
    private final TokenService tokenService;

    public JwksController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/jwks.json")
    public String jwks() {
        return new JWKSet(tokenService.jwkSet().getKeys().getFirst()).toString();
    }
}