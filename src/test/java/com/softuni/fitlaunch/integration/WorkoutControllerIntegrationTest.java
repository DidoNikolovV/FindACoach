package com.softuni.fitlaunch.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.service.UserProgressService;
import com.softuni.fitlaunch.service.UserService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private UserProgressService userProgressService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"CLIENT"})
    public void testAllWorkouts() throws Exception {
        Page<WorkoutDTO> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 6), 0);
        List<LevelEnum> mockLevels = Collections.singletonList(LevelEnum.BEGINNER);

        when(workoutService.getAllWorkouts(any(PageRequest.class))).thenReturn(mockPage);
        when(workoutService.getAllLevels()).thenReturn(mockLevels);

        mockMvc.perform(MockMvcRequestBuilders.get("/workouts/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("workouts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("workouts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("levels"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"CLIENT"})
    public void testLikeWorkout() throws Exception {
        Long programId = 1L;
        Long workoutId = 2L;
        Long weekId = 3L;
        String dayName = "Monday";

        mockMvc.perform(MockMvcRequestBuilders.post("/workouts/{programId}/{workoutId}/weeks/{weekId}/days/{dayName}/like",
                                programId, workoutId, weekId, dayName)
                        .with(csrf())) // Include CSRF token if needed
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/workouts/%d/%d/weeks/%d/days/%s", programId, workoutId, weekId, dayName)));

        verify(workoutService, times(1)).like(workoutId, "testuser");
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"CLIENT"})
    public void testDislikeWorkout() throws Exception {
        Long programId = 1L;
        Long workoutId = 2L;
        Long weekId = 3L;
        String dayName = "Monday";

        mockMvc.perform(MockMvcRequestBuilders.post("/workouts/{programId}/{workoutId}/weeks/{weekId}/days/{dayName}/dislike",
                                programId, workoutId, weekId, dayName)
                        .with(csrf())) // Include CSRF token if needed
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/workouts/%d/%d/weeks/%d/days/%s", programId, workoutId, weekId, dayName)));

        verify(workoutService, times(1)).dislike(workoutId, "testuser");
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"CLIENT"})
    public void testCompleteExercise() throws Exception {
        Long programId = 1L;
        Long workoutId = 2L;
        Long weekId = 3L;
        String dayName = "Monday";
        Long exerciseId = 4L;

        mockMvc.perform(MockMvcRequestBuilders.post("/workouts/{programId}/{workoutId}/weeks/{weekId}/days/{dayName}/exercise/{exerciseId}/complete",
                                programId, workoutId, weekId, dayName, exerciseId)
                        .with(csrf())) // Include CSRF token if needed
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/workouts/%d/%d/weeks/%d/days/%s", programId, workoutId, weekId, dayName)));

        verify(workoutService, times(1)).completeExercise(programId, workoutId, dayName, exerciseId, "testuser", weekId);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"COACH"})
    public void testLoadWorkoutDetails() throws Exception {
        Long workoutId = 1L;
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setId(workoutId);

        when(workoutService.getWorkoutById(workoutId)).thenReturn(workoutDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/workouts/{workoutId}", workoutId)
                        .with(csrf())) // Include CSRF token if needed
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("workout"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("workout"))
                .andExpect(MockMvcResultMatchers.model().attribute("workout", workoutDTO));

        verify(workoutService, times(1)).getWorkoutById(workoutId);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"COACH"})
    public void testCreateWorkout() throws Exception {
        WorkoutCreationDTO workoutCreationDTO = new WorkoutCreationDTO();
        WorkoutDTO createdWorkout = new WorkoutDTO();
        createdWorkout.setId(1L);

        when(workoutService.createWorkout(any(WorkoutCreationDTO.class), eq("testuser"))).thenReturn(createdWorkout);

        mockMvc.perform(MockMvcRequestBuilders.post("/workouts/create")
                        .with(csrf()) // Include CSRF token if needed
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("someField", "someValue")) // Include necessary form parameters
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/workouts/1"));

        verify(workoutService, times(1)).createWorkout(any(WorkoutCreationDTO.class), eq("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"COACH"})
    public void testWorkoutDetails() throws Exception {
        Long programId = 1L;
        Long workoutId = 2L;
        Long weekId = 3L;
        String dayName = "Monday";

        WorkoutDetailsDTO workoutDetailsDTO = new WorkoutDetailsDTO();
        workoutDetailsDTO.setId(workoutId);

        List<WorkoutExerciseDTO> exercises = new ArrayList<>();
        exercises.add(new WorkoutExerciseDTO());

        when(workoutService.getWorkoutDetailsById(workoutId, dayName)).thenReturn(workoutDetailsDTO);
        when(userProgressService.getAllExercisesForWorkout(programId, weekId, workoutId, "testuser", dayName)).thenReturn(exercises);
        when(workoutService.isWorkoutStarted(programId, dayName, workoutId, weekId, "testuser")).thenReturn(true);
        when(workoutService.isWorkoutCompleted(programId, workoutId, weekId, dayName, "testuser")).thenReturn(false);
        when(userService.isWorkoutLiked(workoutId, "testuser")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/workouts/{programId}/{workoutId}/weeks/{weekId}/days/{dayName}",
                                programId, workoutId, weekId, dayName)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Include CSRF token if needed
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("workout-details"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("workout"))
                .andExpect(MockMvcResultMatchers.model().attribute("workout", workoutDetailsDTO))
                .andExpect(MockMvcResultMatchers.model().attribute("hasStarted", true))
                .andExpect(MockMvcResultMatchers.model().attribute("isCompleted", false))
                .andExpect(MockMvcResultMatchers.model().attribute("hasLiked", true))
                .andExpect(MockMvcResultMatchers.model().attribute("exercises", exercises));

        verify(workoutService, times(1)).getWorkoutDetailsById(workoutId, dayName);
        verify(userProgressService, times(1)).getAllExercisesForWorkout(programId, weekId, workoutId, "testuser", dayName);
        verify(workoutService, times(1)).isWorkoutStarted(programId, dayName, workoutId, weekId, "testuser");
        verify(workoutService, times(1)).isWorkoutCompleted(programId, workoutId, weekId, dayName, "testuser");
        verify(userService, times(1)).isWorkoutLiked(workoutId, "testuser");
    }

}