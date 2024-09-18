package chris.project.security.note.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import chris.project.security.note.entity.Note;
import chris.project.security.note.model.NoteModel;
import chris.project.security.note.repository.NoteRepository;
import chris.project.security.user.User;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Note save(NoteModel noteModel) {
        try {
            User user = new User();
            long id = loginUserId();
            user.setId(id);

            Note note = Note.builder()
                    .title(noteModel.getTitle())
                    .description(noteModel.getDescription())
                    .priority(noteModel.getPriority())
                    .user(user)
                    .build();
            noteRepository.save(note);
            return note;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the student.", e);
        }

    }

    @Override
    public List<Note> retrieve() {
        List<Note> notes = noteRepository.findAll();
        if (notes.isEmpty()) {

        }
        return notes;
    }

    @Override
    public List<Note> searchNote(String keyword) {
        List<Note> notes = noteRepository.findByTitleContainingIgnoreCase(keyword);
        if (notes.isEmpty()) {

        }
        return notes;
    }

    @Override
    public Note update(Long id, NoteModel noteModel) {
        Note note = noteRepository.findById(id).get();
        if (Objects.nonNull(id)) {
            note.setTitle(noteModel.getTitle());
            note.setDescription(noteModel.getDescription());
            note.setPriority(noteModel.getPriority());
            return noteRepository.save(note);
        } else {
            throw new RuntimeException("Student not found with id " + id);
        }
    }

    @Override
    public String delete(long id) {
        noteRepository.deleteById(id);
        return "deleted successfully";
    }

    @Override
    public Optional<Note> retrieveById(Long id) {
        return noteRepository.findById(id);
    }

    public long loginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Assuming CustomUserDetails is the principal
        User userDetails = (User) authentication.getPrincipal();
        Long userId = userDetails.getId();
        return userId;
    }

}
