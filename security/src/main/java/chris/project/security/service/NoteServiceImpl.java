package chris.project.security.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import chris.project.security.constant.Priority;
import chris.project.security.entity.Note;
import chris.project.security.entity.User;
import chris.project.security.exception.NoteNotFoundException;
import chris.project.security.repository.NoteRepository;
import chris.project.security.request.NoteModel;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Note save(NoteModel noteModel) {
        try {
            long id = loginUserId();
            Note note = Note.builder()
                    .title(noteModel.getTitle())
                    .description(noteModel.getDescription())
                    .priority(noteModel.getPriority())
                    .build();
            note.setUserId(id);
            noteRepository.save(note);
            return note;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the note.", e);
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
    public List<Note> searchByKeyword(String keyword) {
        long id = loginUserId();
        List<Note> notes = noteRepository.searchByKeyword(keyword);
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
            throw new RuntimeException("Note not found with id " + id);
        }
    }

    @Override
    public String delete(long id) {
        try {
            if (!noteRepository.existsById(id)) {
                return "Note with ID " + id + " not found";
            }
            noteRepository.deleteById(id);
            return "Deleted successfully";
        } catch (Exception e) {
            return "An error occurred while deleting the note: " + e.getMessage();
        }
    }

    @Override
    public Note retrieveById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));
    }

    public long loginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Assuming CustomUserDetails is the principal
        User userDetails = (User) authentication.getPrincipal();
        Long userId = userDetails.getId();
        return userId;
    }

    @Override
    public List<Note> retrieveByDateAsc() {
        try {
            return noteRepository.findAllByOrderByCreatedAtAsc();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving notes: " + e.getMessage());
        }
    }

    @Override
    public List<Note> retrieveByDateDesc() {
        try {
            return noteRepository.findAllByOrderByCreatedAtDesc();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving notes: " + e.getMessage());
        }

    }

    @Override
    public List<Note> findByPriority(Priority priority) {
        try {
            return noteRepository.findByPriority(priority);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving notes: " + e.getMessage());
        }

    }

    @Override
    public List<Note> retrieveByPriorityAsc() {
        try {
            return noteRepository.findAllByOrderByCustomPriorityAsc();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving notes: " + e.getMessage());
        }

    }

    @Override
    public List<Note> retrieveByPriorityDesc() {
        try {
            return noteRepository.findAllByOrderByPriorityDesc();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving notes: " + e.getMessage());
        }
    }

}
