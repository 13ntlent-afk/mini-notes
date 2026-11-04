package com.rocku.gateway.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class LoginHandler {
    private final WebClient authClient;

    public LoginHandler(WebClient.Builder webClientBuilder,
            @Value("${AUTH_SERVICE_URL}") String authServiceUrl) {
        this.authClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @GetMapping("/bff/csrf")
    public Mono<Map<String, String>> csrf(Mono<CsrfToken> csrfToken) {
        return csrfToken.map(token -> Map.of("token", token.getToken()));
    }

    @PostMapping("/bff/login")
    public Mono<ResponseEntity<UserView>> login(@Valid @RequestBody LoginRequest request,
            ServerWebExchange exchange) {
        return authClient.post().uri("/auth/token")
                .bodyValue(new Credentials(request.username(), request.password()))
                .retrieve().bodyToMono(TokenResult.class)
                .flatMap(token -> exchange.getSession().flatMap(session -> session.changeSessionId()
                        .then(Mono.fromSupplier(() -> {
                    var user = new UserView(token.userId(), token.username(), token.roles());
                    session.getAttributes().put(BffSessionAttributes.ACCESS_TOKEN, token.accessToken());
                    session.getAttributes().put(BffSessionAttributes.USER, user);
                    return ResponseEntity.ok(user);
                }))))
                .onErrorResume(WebClientResponseException.class, exception -> Mono.just(
                        ResponseEntity.status(exception.getStatusCode()).<UserView>build()));
    }

    @GetMapping("/bff/me")
    public Mono<ResponseEntity<UserView>> me(ServerWebExchange exchange) {
        return exchange.getSession().map(session -> {
            var user = session.<UserView>getAttribute(BffSessionAttributes.USER);
            return user == null
                    ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).<UserView>build()
                    : ResponseEntity.ok(user);
        });
    }

    @PostMapping("/bff/logout")
    public Mono<ResponseEntity<Void>> logout(ServerWebExchange exchange) {
        return exchange.getSession().flatMap(session -> session.invalidate())
                .thenReturn(ResponseEntity.noContent().build());
    }

    public record LoginRequest(@NotBlank @Size(max = 80) String username,
            @NotBlank @Size(max = 128) String password) {}
    public record Credentials(String username, String password) {}
    public record UserView(Long id, String username, List<String> roles) {}
    public record TokenResult(
            @JsonProperty("accessToken") String accessToken,
            @JsonProperty("tokenType") String tokenType,
            @JsonProperty("expiresIn") long expiresIn,
            @JsonProperty("userId") Long userId,
            @JsonProperty("username") String username,
            @JsonProperty("roles") List<String> roles) {}
}