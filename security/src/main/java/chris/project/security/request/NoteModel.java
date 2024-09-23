package chris.project.security.request;

import chris.project.security.constant.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteModel {

    @Valid
    @NotBlank(message = "Title is Required")
    private String title;
    @NotBlank(message = "Title is Required")
    private String description;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority is Required")
    private Priority priority;

}
