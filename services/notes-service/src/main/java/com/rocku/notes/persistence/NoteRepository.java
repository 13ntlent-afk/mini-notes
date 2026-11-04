package com.rocku.notes.persistence;

import com.rocku.notes.domain.Note;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserIdOrderByUpdatedAtDesc(Long userId);
    Optional<Note> findByIdAndUserId(Long id, Long userId);
}