package com.rocku.contact.api;

import com.rocku.contact.domain.ContactMessage;
import com.rocku.contact.domain.ContactService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> submit(@Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        var userId = Long.valueOf(jwt.getClaimAsString("uid"));
        var message = contactService.submit(userId, request.name(), request.email(),
                request.subject(), request.message());
        return ResponseEntity.created(URI.create("/api/admin/messages/" + message.getId()))
                .body(MessageResponse.from(message));
    }

    public record MessageRequest(
            @NotBlank @Size(max = 100) String name,
            @NotBlank @Email @Size(max = 255) String email,
            @Size(max = 150) String subject,
            @NotBlank @Size(max = 5000) String message) {}

    public record MessageResponse(Long id, Long userId, String name, String email, String subject,
            String message, Instant createdAt) {
        public static MessageResponse from(ContactMessage entity) {
            return new MessageResponse(entity.getId(), entity.getUserId(), entity.getName(), entity.getEmail(),
                    entity.getSubject(), entity.getMessage(), entity.getCreatedAt());
        }
    }
}