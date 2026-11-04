package com.rocku.notes.api.dto;

import com.rocku.notes.domain.Note;
import java.time.Instant;
import java.util.UUID;

public record NoteResponse(Long id, String title, String body, Instant createdAt, Instant updatedAt) {
    public static NoteResponse from(Note note) {
        return new NoteResponse(note.getId(), note.getTitle(), note.getBody(),
                note.getCreatedAt(), note.getUpdatedAt());
    }
}