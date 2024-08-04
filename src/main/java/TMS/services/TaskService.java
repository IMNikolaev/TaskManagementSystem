package TMS.services;

import TMS.dto.TaskDTO;
import TMS.entities.Task;
import TMS.repositiories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;


    public ResponseEntity<Task> createTask(TaskDTO taskDTO, Long userId) {
        return null;
    }
}
