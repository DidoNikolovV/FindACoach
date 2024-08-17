package com.softuni.fitlaunch.integration;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.service.ExerciseService;
import com.softuni.fitlaunch.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private WorkoutService workoutService;

    @Test
    void testLoadExercises() throws Exception {
        WorkoutExerciseDTO dto = new WorkoutExerciseDTO();
        dto.setId(1L);
        dto.setName("Test Exercise");
        Page<WorkoutExerciseDTO> exercises = new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 10), 1);
        when(exerciseService.loadAllExercises(any())).thenReturn(exercises);

        mockMvc.perform(get("/api/v1/workout")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateWorkout() throws Exception {
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setId(1L);
        workoutDTO.setName("Test Workout");

        MockMultipartFile imgFile = new MockMultipartFile("imgUrl", "test.png", "image/png", "test image".getBytes());

        when(workoutService.createWorkout(anyString(), anyString(), any(), anyString(), any())).thenReturn(workoutDTO);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/workout/create")
                        .param("name", "Test Workout")
                        .param("level", "Beginner")
                        .param("exercises", "exercise1", "exercise2")
                        .param("imgUrl", String.valueOf(imgFile))
                        .principal(() -> "testUser"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
