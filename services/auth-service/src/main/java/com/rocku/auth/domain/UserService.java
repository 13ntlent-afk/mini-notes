package com.rocku.auth.domain;

import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String password) {
        return create(username, password, "USER");
    }

    public User authenticate(String username, String password) {
        var normalized = normalize(username);
        var user = userRepository.findByUsername(normalized).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return user;
    }

    public void ensureAdmin(String username, String password) {
        var normalized = normalize(username);
        var existing = userRepository.findByUsername(normalized).orElse(null);
        if (existing != null) {
            if (!"ADMIN".equals(existing.getRole())) {
                throw new IllegalStateException("Bootstrap username exists without ADMIN role");
            }
            return;
        }
        create(normalized, password, "ADMIN");
    }

    private User create(String username, String password, String role) {
        var normalized = normalize(username);
        if (userRepository.findByUsername(normalized).isPresent()) {
            throw new AuthException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (password == null || password.length() < 12 || password.length() > 128) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Password must be 12 to 128 characters");
        }
        return userRepository.save(new User(UUID.randomUUID(), normalized,
                passwordEncoder.encode(password), role));
    }

    private String normalize(String username) {
        if (username == null || !username.matches("[A-Za-z0-9._-]{3,80}")) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Username must be 3 to 80 letters, digits, or ._- characters");
        }
        return username.toLowerCase(Locale.ROOT);
    }
}