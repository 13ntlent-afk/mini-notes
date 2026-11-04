package com.rocku.contact.api;

import com.rocku.contact.domain.ContactService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/messages")
public class AdminMessagesController {
    private final ContactService contactService;

    public AdminMessagesController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContactController.MessageResponse> list() {
        return contactService.inbox().stream().map(ContactController.MessageResponse::from).toList();
    }
}