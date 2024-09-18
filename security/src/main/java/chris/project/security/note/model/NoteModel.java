package chris.project.security.note.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteModel {

    @NotBlank(message = "Title is Required")
    private String title;
    @NotBlank(message = "Title is Required")
    @Enumerated(EnumType.STRING)
    private String description;
    private Priority priority;

}
