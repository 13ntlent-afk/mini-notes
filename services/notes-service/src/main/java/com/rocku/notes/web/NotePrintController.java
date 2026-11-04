package com.rocku.notes.web;

import com.rocku.notes.api.dto.NoteResponse;
import com.rocku.notes.domain.NoteService;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/print")
public class NotePrintController {
    private final NoteService noteService;

    public NotePrintController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_notes:read')")
    public String print(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt, Model model) {
        var userId = UUID.fromString(jwt.getClaimAsString("uid"));
        model.addAttribute("note", NoteResponse.from(noteService.findForUser(id, userId)));
        return "print-note";
    }
}