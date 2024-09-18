package chris.project.security.note.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chris.project.security.note.entity.Note;
import chris.project.security.note.model.NoteModel;
import chris.project.security.note.service.NoteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class NoteController {

    @Autowired
    private NoteService NoteService;

    @GetMapping("/note/hi")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello Testing 123");
    }

    @PostMapping("/note/save")
    public ResponseEntity<?> create(@Valid @RequestBody NoteModel noteModel) {
        Note note = NoteService.save(noteModel);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/note/retrieve")
    public ResponseEntity<?> retrieve() {
        return ResponseEntity.ok(NoteService.retrieve());

    }

    @GetMapping("/note/retrieveById/{id}")
    public ResponseEntity<?> retrieveById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(NoteService.retrieveById(id));
    }

    @GetMapping("/note/retrieve/{keyword}")
    public ResponseEntity<?> search(@PathVariable String keyword) {
        List<Note> foundNotes = NoteService.searchNote(keyword);
        return ResponseEntity.ok(foundNotes);

    }

    @PutMapping("/note/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NoteModel noteModel) {
        Note note = NoteService.update(id, noteModel);
        return ResponseEntity.ok(note);

    }

    @DeleteMapping("/note/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        return NoteService.delete(id);

    }

}
