package TMS.controllers;

import TMS.dto.CommentDTO;
import TMS.entities.User;
import TMS.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Создать комментарий")
    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestBody CommentDTO commentDTO,
            @RequestParam Long taskId,
            @AuthenticationPrincipal User user) {
        return commentService.createComment(commentDTO, taskId, user);
    }

    @Operation(summary = "Получить комментарий по ID")
    @GetMapping("{id}")
    public ResponseEntity<CommentDTO> getCommentById(
            @PathVariable("id") Long id) {
        return commentService.getCommentById(id);
    }

    @Operation(summary = "Удалить комментарий по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user) {
        return commentService.deleteComment(id, user);
    }

    @Operation(summary = "Обновить комментарий по ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommentById(
            @PathVariable("id") Long id,
            @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal User user) {
        return commentService.updateComment(id, commentDTO, user);
    }

    @Operation(summary = "Получить все комментарии с учетом фильтров")
    @GetMapping
    public ResponseEntity<Page<CommentDTO>> getAllComments(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.getAllComments(userId, taskId, page, size);
    }
}
