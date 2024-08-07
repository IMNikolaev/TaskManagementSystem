package TMS.controllers;

import TMS.dto.TaskDTO;
import TMS.entities.User;
import TMS.entities.enums.PriorityEnum;
import TMS.entities.enums.StatusEnum;
import TMS.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @MockBean
    private TaskService taskService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateTask() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .build();

        TaskDTO taskDTO = TaskDTO.builder()
                .title("New Task")
                .description("Task Description")
                .priorityEnum(PriorityEnum.HIGH)
                .statusEnum(StatusEnum.IN_PROGRESS)
                .build();

        given(taskService.createTask(taskDTO, user)).willReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/api/v1/tasks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(taskDTO))
                .principal(() -> user.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTaskById() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder()
                .title("Task Title")
                .description("Task Description")
                .priorityEnum(PriorityEnum.MEDIUM)
                .statusEnum(StatusEnum.IN_PROGRESS)
                .build();

        given(taskService.getTaskById(1L)).willReturn(ResponseEntity.ok(taskDTO));

        mockMvc.perform(get("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task Title"))
                .andExpect(jsonPath("$.description").value("Task Description"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteTaskById() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .build();

        given(taskService.deleteTask(1L, user)).willReturn(ResponseEntity.ok("Task deleted"));

        mockMvc.perform(delete("/api/v1/tasks/1")
                .principal(() -> user.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateTaskById() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .build();

        TaskDTO taskDTO = TaskDTO.builder()
                .title("Updated Task")
                .description("Updated Description")
                .priorityEnum(PriorityEnum.LOW)
                .statusEnum(StatusEnum.COMPLETED)
                .build();

        given(taskService.updateTask(1L, taskDTO, user)).willReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/api/v1/tasks/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(taskDTO))
                .principal(() -> user.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAllTasks() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder()
                .title("Task Title")
                .description("Task Description")
                .priorityEnum(PriorityEnum.MEDIUM)
                .statusEnum(StatusEnum.IN_PROGRESS)
                .build();

        Page<TaskDTO> taskPage = new PageImpl<>(List.of(taskDTO), PageRequest.of(0, 10), 1);

        given(taskService.findAll(null, null, 0, 10)).willReturn(taskPage);

        mockMvc.perform(get("/api/v1/tasks")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Task Title"))
                .andExpect(jsonPath("$.content[0].description").value("Task Description"));
    }
}
