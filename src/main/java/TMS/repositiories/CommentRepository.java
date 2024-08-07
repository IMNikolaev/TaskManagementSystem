package TMS.repositiories;

import TMS.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE (:userId IS NULL OR c.author.id = :userId) " +
            "AND (:taskId IS NULL OR c.task.id = :taskId) ")
    Page<Comment> findAllByFilters(Long userId, Long taskId, Pageable pageable);
}
