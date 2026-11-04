package com.rocku.contact.api;

import com.rocku.contact.domain.ContactMessage;
import com.rocku.contact.domain.ContactService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MessageResponse> submit(@Valid @RequestBody MessageRequest request) {
        var message = contactService.submit(request.name(), request.email(), request.message());
        return ResponseEntity.created(URI.create("/api/admin/messages/" + message.getId()))
                .body(MessageResponse.from(message));
    }

    public record MessageRequest(
            @NotBlank @Size(max = 120) String name,
            @NotBlank @Email @Size(max = 254) String email,
            @NotBlank @Size(max = 5000) String message) {}

    public record MessageResponse(UUID id, String name, String email, String message, Instant receivedAt) {
        public static MessageResponse from(ContactMessage entity) {
            return new MessageResponse(entity.getId(), entity.getName(), entity.getEmail(),
                    entity.getMessage(), entity.getReceivedAt());
        }
    }
}