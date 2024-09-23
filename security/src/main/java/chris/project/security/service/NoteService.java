package chris.project.security.service;

import java.util.List;

import chris.project.security.constant.Priority;
import chris.project.security.entity.Note;
import chris.project.security.request.NoteModel;

public interface NoteService {

    public Note save(NoteModel noteModel);

    public List<Note> retrieve();

    public Note retrieveById(Long id);

    public List<Note> retrieveByDateAsc();

    public List<Note> retrieveByDateDesc();

    public List<Note> searchByKeyword(String keyword);

    public Note update(Long id, NoteModel noteModel);

    public String delete(long id);

    public List<Note> retrieveByPriorityAsc();

    public List<Note> retrieveByPriorityDesc();

    public List<Note> findByPriority(Priority priority);

}
