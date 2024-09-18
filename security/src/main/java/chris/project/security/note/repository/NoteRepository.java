package chris.project.security.note.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import chris.project.security.note.entity.Note;
import chris.project.security.note.model.Priority;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT DISTINCT n FROM Note n WHERE "
            + "LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(n.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(n.priority) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Note> searchByKeyword(@Param("keyword") String keyword);

    List<Note> findAllByOrderByCreatedAtAsc();

    List<Note> findAllByOrderByCreatedAtDesc();

    List<Note> findByPriority(Priority priority);

    @Query("SELECT n FROM Note n ORDER BY CASE WHEN n.priority = 'LOW' THEN 1 WHEN n.priority = 'MEDIUM' THEN 2 ELSE 3 END ASC")
    List<Note> findAllByOrderByCustomPriorityAsc();

    @Query("SELECT n FROM Note n ORDER BY CASE WHEN n.priority = 'HIGH' THEN 3 WHEN n.priority = 'MEDIUM' THEN 2 ELSE 1 END DESC")
    List<Note> findAllByOrderByPriorityDesc();
}
