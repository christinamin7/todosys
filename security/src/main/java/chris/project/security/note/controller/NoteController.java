package chris.project.security.note.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import chris.project.security.note.model.Priority;
import chris.project.security.note.service.NoteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class NoteController {

    @Autowired
    private NoteService NoteService;

    @GetMapping("/note/test")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello Testing 123");
    }

    @PostMapping("/note/save")
    public ResponseEntity<?> create(@Valid @RequestBody NoteModel noteModel) {
        Note note = NoteService.save(noteModel);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/note/retrieve")
    public ResponseEntity<?> retrieve() {
        return ResponseEntity.ok(NoteService.retrieve());

    }

    @GetMapping("/note/retrieveById/{id}")
    public ResponseEntity<?> retrieveById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(NoteService.retrieveById(id));
    }

    @GetMapping("/note/retrieveByDate/{sorting}")
    public ResponseEntity<?> retrieveByDate(@PathVariable("sorting") String sorting) {
        if (sorting.equalsIgnoreCase("desc")) {
            return ResponseEntity.ok(NoteService.retrieveByDateDesc());
        } else {
            return ResponseEntity.ok(NoteService.retrieveByDateAsc());
        }
    }

    @GetMapping("/note/retrieveByPrioritySorting/{sorting}")
    public ResponseEntity<?> retrieveByPrioritySorting(@PathVariable("sorting") String sorting) {
        if (sorting.equalsIgnoreCase("desc")) {
            return ResponseEntity.ok(NoteService.retrieveByPriorityDesc());
        } else {
            return ResponseEntity.ok(NoteService.retrieveByPriorityAsc());
        }
    }

    @GetMapping("/note/retrieve/{keyword}")
    public ResponseEntity<?> search(@PathVariable String keyword) {
        List<Note> foundNotes = NoteService.searchNote(keyword);
        return ResponseEntity.ok(foundNotes);

    }

    @GetMapping("/note/retrieveByPriority/{priority}")
    public ResponseEntity<?> retrieveByPriority(@PathVariable("priority") Priority priority) {
        return ResponseEntity.ok(NoteService.findByPriority(priority));

    }

    @PutMapping("/note/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NoteModel noteModel) {
        Note note = NoteService.update(id, noteModel);
        return new ResponseEntity<>(note, HttpStatus.OK);

    }

    @DeleteMapping("/note/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        NoteService.delete(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);

    }

}
