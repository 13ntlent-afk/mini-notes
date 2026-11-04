package com.rocku.contact.domain;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactService {
    private final ContactMessageRepository repository;

    public ContactService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    public ContactMessage submit(Long userId, String name, String email, String subject, String message) {
        var trimmedSubject = subject == null || subject.isBlank() ? null : subject.trim();
        return repository.save(new ContactMessage(userId, name.trim(), email.trim().toLowerCase(),
                trimmedSubject, message.trim()));
    }

    @Transactional(readOnly = true)
    public List<ContactMessage> inbox() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
}