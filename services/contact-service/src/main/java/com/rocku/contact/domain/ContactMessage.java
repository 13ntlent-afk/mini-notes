package com.rocku.contact.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "contact_messages", schema = "contact")
public class ContactMessage {
    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(name = "message_body", nullable = false, length = 5000)
    private String message;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    protected ContactMessage() {
    }

    public ContactMessage(String name, String email, String message) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.message = message;
        this.receivedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }
    public Instant getReceivedAt() { return receivedAt; }
}