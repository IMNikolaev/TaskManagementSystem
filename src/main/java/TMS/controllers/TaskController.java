package TMS.controllers;

import TMS.dto.TaskDTO;
import TMS.entities.User;
import TMS.entities.enums.PriorityEnum;
import TMS.entities.enums.StatusEnum;
import TMS.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Создать задачу")
    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestBody TaskDTO taskDTO,
            @AuthenticationPrincipal User user) {
        return taskService.createTask(taskDTO, user);
    }

    @Operation(summary = "Получить задачу по ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(
            @PathVariable("id") Long id) {
        return taskService.getTaskById(id);
    }

    @Operation(summary = "Удалить задачу по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user) {
        return taskService.deleteTask(id, user);
    }

    @Operation(summary = "Обновить задачу по ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaskById(
            @PathVariable("id") Long id,
            @RequestBody TaskDTO taskDTO,
            @AuthenticationPrincipal User user) {
        return taskService.updateTask(id, taskDTO, user);
    }

    @Operation(summary = "Получить все задачи c учетом фильтров")
    @GetMapping
    public ResponseEntity<Page<TaskDTO>> findAll(
            @RequestParam(required = false) StatusEnum statusEnum,
            @RequestParam(required = false) PriorityEnum priorityEnum,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        Page<TaskDTO> tasks = taskService.findAll(statusEnum, priorityEnum, page, size);
        return ResponseEntity.ok(tasks);
    }
}
