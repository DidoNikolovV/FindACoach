package com.softuni.fitlaunch.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.softuni.fitlaunch.model.dto.TopicCommentDTO;
import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.view.CommentView;
import com.softuni.fitlaunch.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;


    @Test
    void testGetComment() throws Exception {
        Long workoutId = 1L;
        Long commentId = 1L;

        CommentView commentView = new CommentView();
        commentView.setId(commentId);

        when(commentService.getComment(commentId)).thenReturn(commentView);

        mockMvc.perform(get("/api/v1/comments/{workoutId}/{commentId}", workoutId, commentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(commentId));
    }

    @Test
    void testGetCommentsByWorkoutId() throws Exception {
        Long workoutId = 1L;

        CommentView comment1 = new CommentView();
        comment1.setId(1L);

        CommentView comment2 = new CommentView();
        comment2.setId(2L);

        List<CommentView> comments = List.of(comment1, comment2);

        when(commentService.getAllCommentsForWorkout(workoutId)).thenReturn(comments);

        mockMvc.perform(get("/api/v1/comments/{workoutId}/all", workoutId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testPostComment() throws Exception {
        Long workoutId = 1L;

        CommentCreationDTO commentCreationDTO = new CommentCreationDTO();
        commentCreationDTO.setMessage("New Comment");

        CommentView commentView = new CommentView();
        commentView.setId(1L);
        commentView.setMessage("New Comment");
        commentView.setAuthorUsername("testUser");

        when(commentService.addComment(any(CommentCreationDTO.class), anyString(), anyLong())).thenReturn(commentView);

        String jsonContent = "{\"message\":\"New Comment\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/comments/{workoutId}", workoutId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("New Comment"))
                .andExpect(jsonPath("$.authorUsername").value("testUser"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDeleteComment() throws Exception {
        Long workoutId = 1L;
        Long commentId = 1L;

        mockMvc.perform(delete("/api/v1/comments/{workoutId}/{commentId}", workoutId, commentId)
                        .principal(() -> "testUser"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCommentsByTopicId() throws Exception {
        Long topicId = 1L;

        TopicCommentDTO topicCommentDTO = new TopicCommentDTO();
        topicCommentDTO.setId(1L);

        Page<TopicCommentDTO> page = new PageImpl<>(List.of(topicCommentDTO), PageRequest.of(0, 4), 1);

        when(commentService.findByTopicId(topicId, PageRequest.of(0, 4))).thenReturn(page);

        mockMvc.perform(get("/api/v1/comments/topic/{topicId}/all", topicId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testPostTopicComment() throws Exception {
        Long topicId = 1L;

        CommentCreationDTO commentCreationDTO = new CommentCreationDTO();
        commentCreationDTO.setMessage("New Topic Comment");

        TopicCommentDTO topicCommentDTO = new TopicCommentDTO();
        topicCommentDTO.setId(1L);
        topicCommentDTO.setMessage("New Topic Comment");

        String jsonContent = "{\"message\":\"New Topic Comment\"}";

        when(commentService.addTopicComment(any(), anyString(), anyLong())).thenReturn(topicCommentDTO);

        mockMvc.perform(post("/api/v1/comments/topic/{topicId}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .principal(() -> "testUser"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("New Topic Comment"));
    }
}
