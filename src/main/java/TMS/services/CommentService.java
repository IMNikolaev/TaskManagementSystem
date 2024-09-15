package TMS.services;

import TMS.dto.CommentDTO;
import TMS.entities.Comment;
import TMS.entities.Task;
import TMS.entities.User;
import TMS.repositiories.CommentRepository;
import TMS.repositiories.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public ResponseEntity<?> createComment(CommentDTO commentDTO, Long taskId, User user) {
        Optional<Task> task = taskRepository.findById(taskId);
        if(task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
        Comment comment = Comment.builder()
                .task(task.get())
                .author(user)
                .text(commentDTO.getText())
                .build();
        comment = commentRepository.save(comment);

        CommentDTO result = CommentDTO.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    public ResponseEntity<CommentDTO> getCommentById(Long commentId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if(commentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = commentOpt.get();

        CommentDTO result = CommentDTO.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<String> deleteComment(Long commentId, User user) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if(commentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = commentOpt.get();
        if (!comment.getAuthor().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this comment.");
        }

        commentRepository.delete(comment);

        return ResponseEntity.ok("Comment deleted successfully.");
    }

    public ResponseEntity<?> updateComment(Long commentId, CommentDTO commentDTO, User user) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if(commentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = commentOpt.get();
        if (!comment.getAuthor().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to update this comment.");
        }
        comment.setText(commentDTO.getText());
        comment = commentRepository.save(comment);

        CommentDTO result = CommentDTO.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Page<CommentDTO>> getAllComments(Long userId, Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findAllByFilters(userId, taskId, pageable);
        List<CommentDTO> commentDTOs = commentsPage.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        Page<CommentDTO> resultPage = new PageImpl<>(commentDTOs, pageable, commentsPage.getTotalElements());
        return ResponseEntity.ok(resultPage);
    }

    private CommentDTO convertToDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .authorId(comment.getAuthor().getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
