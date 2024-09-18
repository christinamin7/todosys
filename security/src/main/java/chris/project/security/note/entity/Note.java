package chris.project.security.note.entity;

import java.util.Date;

import chris.project.security.note.model.Priority;
import chris.project.security.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ID"))
    private User user;
    // Automatically set createdAt before persisting

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    // Automatically update updatedAt before updating the entity
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public void setUserId(Long userId) {
        this.user = new User();
        this.user.setId(userId);  // Only set the ID
    }

}
