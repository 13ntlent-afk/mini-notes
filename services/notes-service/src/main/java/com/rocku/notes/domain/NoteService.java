package com.rocku.notes.domain;

import com.rocku.notes.persistence.NoteRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional(readOnly = true)
    public List<Note> listForUser(Long userId) {
        return noteRepository.findAllByUserIdOrderByUpdatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public Note findForUser(Long id, Long userId) {
        return noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    public Note create(Long userId, String title, String body) {
        return noteRepository.save(new Note(Long.randomLong(), userId, title.trim(), body));
    }

    public Note update(Long id, Long userId, String title, String body) {
        var note = findForUser(id, userId);
        note.edit(title.trim(), body);
        return note;
    }

    public void delete(Long id, Long userId) {
        noteRepository.delete(findForUser(id, userId));
    }
}