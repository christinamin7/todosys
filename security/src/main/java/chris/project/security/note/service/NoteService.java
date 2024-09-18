package chris.project.security.note.service;

import java.util.List;
import java.util.Optional;

import chris.project.security.note.entity.Note;
import chris.project.security.note.model.NoteModel;

public interface NoteService {

    Note save(NoteModel noteModel);

    List<Note> retrieve();

    Optional<Note> retrieveById(Long id);

    List<Note> searchNote(String keyword);

    Note update(Long id, NoteModel noteModel);

    String delete(long id);

}
