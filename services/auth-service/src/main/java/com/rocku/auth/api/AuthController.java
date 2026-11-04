package com.rocku.auth.api;

import com.rocku.auth.domain.TokenService;
import com.rocku.auth.domain.User;
import com.rocku.auth.domain.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody Registration registration) {
        var user = userService.register(registration.username(), registration.email(), registration.password());
        return ResponseEntity.created(URI.create("/auth/users/" + user.getId())).body(UserResponse.from(user));
    }

    @PostMapping("/token")
    public TokenService.TokenResponse token(@Valid @RequestBody Credentials credentials) throws Exception {
        return tokenService.issue(userService.authenticate(credentials.username(), credentials.password()));
    }

    public record Credentials(
            @NotBlank @Pattern(regexp = "[A-Za-z0-9._-]{3,50}") String username,
            @NotBlank @Size(min = 12, max = 128) String password) {}

    public record Registration(
            @NotBlank @Pattern(regexp = "[A-Za-z0-9._-]{3,50}") String username,
            @NotBlank @Email @Size(max = 255) String email,
            @NotBlank @Size(min = 12, max = 128) String password) {}

    public record UserResponse(Long id, String username, String email, List<String> roles, Instant createdAt) {
        static UserResponse from(User user) {
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
                    List.of(user.getRole()), user.getCreatedAt());
        }
    }
}