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

    public ContactMessage submit(String name, String email, String message) {
        return repository.save(new ContactMessage(name.trim(), email.trim().toLowerCase(), message.trim()));
    }

    @Transactional(readOnly = true)
    public List<ContactMessage> inbox() {
        return repository.findAllByOrderByReceivedAtDesc();
    }
}