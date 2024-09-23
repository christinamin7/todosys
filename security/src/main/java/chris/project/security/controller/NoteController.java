package chris.project.security.controller;

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

import chris.project.security.constant.Priority;
import chris.project.security.request.NoteModel;
import chris.project.security.service.NoteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/note/save")
    public ResponseEntity<?> create(@Valid @RequestBody NoteModel noteModel) {
        try {
            return ResponseEntity.ok(noteService.save(noteModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
        }
    }

    @GetMapping("/note/retrieve")
    public ResponseEntity<?> retrieve() {
        try {
            return ResponseEntity.ok(noteService.retrieve());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
        }
    }

    @GetMapping("/note/retrieveById/{id}")
    public ResponseEntity<?> retrieveById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(noteService.retrieveById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
        }
    }

    @GetMapping("/note/retrieveByDate/{sorting}")
    public ResponseEntity<?> retrieveByDate(@PathVariable("sorting") String sorting) {
        if (sorting.equalsIgnoreCase("desc")) {
            try {
                return ResponseEntity.ok(noteService.retrieveByDateDesc());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
            }

        } else {
            try {
                return ResponseEntity.ok(noteService.retrieveByDateDesc());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
            }
        }
    }

    @GetMapping("/note/retrieveByPrioritySorting/{sorting}")
    public ResponseEntity<?> retrieveByPrioritySorting(@PathVariable("sorting") String sorting) {
        if (sorting.equalsIgnoreCase("desc")) {
            try {
                return ResponseEntity.ok(noteService.retrieveByPriorityDesc());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
            }
        } else {
            try {
                return ResponseEntity.ok(noteService.retrieveByPriorityAsc());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
            }
        }
    }

    @GetMapping("/note/retrieve/{keyword}")
    public ResponseEntity<?> searchByKeyword(@PathVariable String keyword) {
        try {
            return ResponseEntity.ok(noteService.searchByKeyword(keyword));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
        }

    }

    @GetMapping("/note/retrieveByPriority/{priority}")
    public ResponseEntity<?> retrieveByPriority(@PathVariable("priority") Priority priority) {
        try {
            return ResponseEntity.ok(noteService.findByPriority(priority));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
        }

    }

    @PutMapping("/note/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NoteModel noteModel) {
        try {
            return ResponseEntity.ok(noteService.update(id, noteModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
        }

    }

    @DeleteMapping("/note/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(noteService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("An error occoured " + e.getMessage()));
        }

    }

}
