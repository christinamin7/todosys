package chris.project.security.note.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import chris.project.security.note.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByTitleContainingIgnoreCase(String title);

}
