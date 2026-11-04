package com.rocku.notes.api.dto;

import com.rocku.notes.domain.Note;
import java.time.Instant;

public record NoteResponse(Long id, String title, String body, Instant createdAt, Instant updatedAt) {
    public static NoteResponse from(Note note) {
        // notes.body is nullable in the DDL; the UI expects a string.
        return new NoteResponse(note.getId(), note.getTitle(), note.getBody() == null ? "" : note.getBody(),
                note.getCreatedAt(), note.getUpdatedAt());
    }
}