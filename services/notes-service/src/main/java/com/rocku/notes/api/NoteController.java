package com.rocku.notes.api;

import com.rocku.notes.api.dto.NoteRequest;
import com.rocku.notes.api.dto.NoteResponse;
import com.rocku.notes.domain.NoteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_notes:read')")
    public List<NoteResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return noteService.listForUser(userId(jwt)).stream().map(NoteResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_notes:read')")
    public NoteResponse get(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return NoteResponse.from(noteService.findForUser(id, userId(jwt)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_notes:write')")
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        var created = noteService.create(userId(jwt), request.title(), request.body());
        return ResponseEntity.created(URI.create("/api/notes/" + created.getId()))
                .body(NoteResponse.from(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_notes:write')")
    public NoteResponse update(@PathVariable Long id, @Valid @RequestBody NoteRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return NoteResponse.from(noteService.update(id, userId(jwt), request.title(), request.body()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_notes:write')")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        noteService.delete(id, userId(jwt));
        return ResponseEntity.noContent().build();
    }

    private Long userId(Jwt jwt) {
        return Long.fromString(jwt.getClaimAsString("uid"));
    }
}