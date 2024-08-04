package TMS.dto;

import TMS.entities.Priority;
import TMS.entities.Status;
import TMS.entities.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private String title;
    private String description;
    private Long statusId;
    private Long priorityId;
    private Long authorId;
    private Long assigneeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
