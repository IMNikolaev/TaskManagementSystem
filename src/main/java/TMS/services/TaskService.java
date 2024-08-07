package TMS.services;

import TMS.dto.TaskDTO;
import TMS.entities.Task;
import TMS.entities.User;
import TMS.entities.enums.PriorityEnum;
import TMS.entities.enums.StatusEnum;
import TMS.repositiories.TaskRepository;
import TMS.services.auth.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;


    @Transactional
    public ResponseEntity<?> createTask(TaskDTO taskDTO, User user) {
        if (taskDTO.getTitle() == null || taskDTO.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Title must not be empty.");
        }

        User assignee = userService.findById(taskDTO.getAssigneeId());
        if (assignee == null) {
            return ResponseEntity.badRequest().body("Assignee not found.");
        }

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .statusEnum(taskDTO.getStatusEnum())
                .priorityEnum(taskDTO.getPriorityEnum())
                .author(user)
                .assignee(assignee)
                .build();

        task = taskRepository.save(task);

        TaskDTO result = TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .statusEnum(task.getStatusEnum())
                .priorityEnum(task.getPriorityEnum())
                .authorId(task.getAuthor().getId())
                .assigneeId(task.getAssignee().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<TaskDTO> getTaskById(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskOpt.get();

        TaskDTO result = TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .statusEnum(task.getStatusEnum())
                .priorityEnum(task.getPriorityEnum())
                .authorId(task.getAuthor().getId())
                .assigneeId(task.getAssignee().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<String> deleteTask(Long taskId, User user) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskOpt.get();
        if (!task.getAuthor().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this task.");
        }

        taskRepository.delete(task);

        return ResponseEntity.ok("Task deleted successfully.");
    }

    public ResponseEntity<?> updateTask(Long taskId, TaskDTO taskDTO, User user) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskOpt.get();

        boolean isAuthor = task.getAuthor().getId().equals(user.getId());
        boolean isAssignee = task.getAssignee().getId().equals(user.getId());

        if (!isAuthor && !isAssignee) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to update this task.");
        }

        if (isAuthor) {
            if (taskDTO.getTitle() != null && !taskDTO.getTitle().trim().isEmpty()) {
                task.setTitle(taskDTO.getTitle());
            }
            if (taskDTO.getDescription() != null) {
                task.setDescription(taskDTO.getDescription());
            }
            if (taskDTO.getStatusEnum() != null) {
                task.setStatusEnum(taskDTO.getStatusEnum());
            }
            if (taskDTO.getPriorityEnum() != null) {
                task.setPriorityEnum(taskDTO.getPriorityEnum());
            }
            if (taskDTO.getAssigneeId() != null) {
                User assigneeOpt = userService.findById(taskDTO.getAssigneeId());
                if (assigneeOpt.getId() != null) {
                    return ResponseEntity.badRequest().body("Assignee not found.");
                }
                task.setAssignee(assigneeOpt);
            }
        } else if (isAssignee) {
            if (taskDTO.getStatusEnum() != null) {
                task.setStatusEnum(taskDTO.getStatusEnum());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update the status of your assigned task.");
            }
        }

        task = taskRepository.save(task);

        TaskDTO result = TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .statusEnum(task.getStatusEnum())
                .priorityEnum(task.getPriorityEnum())
                .authorId(task.getAuthor().getId())
                .assigneeId(task.getAssignee().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();

        return ResponseEntity.ok(result);
    }

    public Page<TaskDTO> findAll(StatusEnum statusEnum, PriorityEnum priorityEnum, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks;

        if (statusEnum != null && priorityEnum != null) {
            tasks = taskRepository.findAllByStatusEnumAndPriorityEnum(statusEnum, priorityEnum, pageable);
        } else if (statusEnum != null) {
            tasks = taskRepository.findAllByStatusEnum(statusEnum, pageable);
        } else if (priorityEnum != null) {
            tasks = taskRepository.findAllByPriorityEnum(priorityEnum, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }

        return tasks.map(task -> TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .statusEnum(task.getStatusEnum())
                .priorityEnum(task.getPriorityEnum())
                .authorId(task.getAuthor().getId())
                .assigneeId(task.getAssignee().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build());
    }
}
