package com.rocku.notes.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteRequest(
        @NotBlank @Size(max = 160) String title,
        @NotNull @Size(max = 20000) String body) {
}