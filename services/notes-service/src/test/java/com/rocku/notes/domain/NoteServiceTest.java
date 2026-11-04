package com.rocku.notes.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rocku.notes.persistence.NoteRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void cannotReadNoteOwnedByAnotherUser() {
        var noteId = 10L;
        var userId = 20L;
        when(noteRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.findForUser(noteId, userId));
        verify(noteRepository).findByIdAndUserId(noteId, userId);
    }
}