package chris.project.security.note.model;

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
    private String description;
    private String priority;

}
