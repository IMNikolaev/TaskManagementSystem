package TMS.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long taskId;
    private Long authorId;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
