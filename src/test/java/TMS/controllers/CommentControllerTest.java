package TMS.controllers;

import TMS.dto.CommentDTO;
import TMS.entities.User;
import TMS.services.CommentService;
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
public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @MockBean
    private CommentService commentService;

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
    public void testCreateComment() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .build();

        CommentDTO commentDTO = CommentDTO.builder()
                .text("This is a comment")
                .build();

        given(commentService.createComment(commentDTO, 1L, user)).willReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/api/v1/comments")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(commentDTO))
                .param("taskId", "1")
                .principal(() -> user.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCommentById() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder()
                .text("This is a comment")
                .build();

        given(commentService.getCommentById(1L)).willReturn(ResponseEntity.ok(commentDTO));

        mockMvc.perform(get("/api/v1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("This is a comment"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteCommentById() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .build();

        given(commentService.deleteComment(1L, user)).willReturn(ResponseEntity.ok("Comment deleted"));

        mockMvc.perform(delete("/api/v1/comments/1")
                .principal(() -> user.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateCommentById() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .build();

        CommentDTO commentDTO = CommentDTO.builder()
                .text("Updated comment")
                .build();

        given(commentService.updateComment(1L, commentDTO, user)).willReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/api/v1/comments/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(commentDTO))
                .principal(() -> user.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllComments() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder()
                .text("This is a comment")
                .build();

        Page<CommentDTO> commentPage = new PageImpl<>(List.of(commentDTO), PageRequest.of(0, 10), 1);

        given(commentService.getAllComments(null, null, 0, 10)).willReturn(ResponseEntity.ok(commentPage));

        mockMvc.perform(get("/api/v1/comments")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].text").value("This is a comment"));
    }
}
