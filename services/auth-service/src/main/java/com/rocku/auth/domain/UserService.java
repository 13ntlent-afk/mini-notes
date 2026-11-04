package com.rocku.auth.domain;

import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]{1,64}@[^@\\s]+\\.[^@\\s]+$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String email, String password) {
        return create(username, email, password, "USER");
    }

    public User authenticate(String username, String password) {
        var normalized = normalize(username);
        var user = userRepository.findByUsername(normalized).orElse(null);
        if (user == null || !user.isEnabled() || user.getPasswordHash() == null
                || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return user;
    }

    public void ensureAdmin(String username, String email, String password) {
        var normalized = normalize(username);
        var existing = userRepository.findByUsername(normalized).orElse(null);
        if (existing != null) {
            if (!"ADMIN".equals(existing.getRole())) {
                throw new IllegalStateException("Bootstrap username exists without ADMIN role");
            }
            return;
        }
        create(normalized, email, password, "ADMIN");
    }

    private User create(String username, String email, String password, String role) {
        var normalized = normalize(username);
        var normalizedEmail = normalizeEmail(email);
        if (userRepository.findByUsername(normalized).isPresent()) {
            throw new AuthException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new AuthException(HttpStatus.CONFLICT, "Email already exists");
        }
        if (password == null || password.length() < 12 || password.length() > 128) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Password must be 12 to 128 characters");
        }
        return userRepository.save(new User(normalized, normalizedEmail,
                passwordEncoder.encode(password), role));
    }

    private String normalize(String username) {
        if (username == null || !username.matches("[A-Za-z0-9._-]{3,50}")) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Username must be 3 to 50 letters, digits, or ._- characters");
        }
        return username.toLowerCase(Locale.ROOT);
    }

    private String normalizeEmail(String email) {
        if (email == null || email.length() > 255 || !EMAIL.matcher(email.trim()).matches()) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "A valid email of up to 255 characters is required");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}