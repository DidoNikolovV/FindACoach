package com.softuni.fitlaunch.integration;

import com.softuni.fitlaunch.model.dto.view.ScheduledWorkoutView;
import com.softuni.fitlaunch.model.dto.workout.WorkoutRequest;
import com.softuni.fitlaunch.service.ScheduleWorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduledWorkoutsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleWorkoutService scheduleWorkoutService;

    @Test
    @WithMockUser("testUser")
    public void testGetAllScheduledWorkouts() throws Exception {
        ScheduledWorkoutView workout1 = new ScheduledWorkoutView();
        workout1.setId(1L);
        workout1.setClientName("testUser");
        workout1.setScheduledDateTime("2024-08-17");

        ScheduledWorkoutView workout2 = new ScheduledWorkoutView();
        workout2.setId(2L);
        workout2.setClientName("testUser");
        workout2.setScheduledDateTime("2024-08-18");

        List<ScheduledWorkoutView> workouts = List.of(workout1, workout2);

        when(scheduleWorkoutService.getAllScheduledWorkouts(anyString())).thenReturn(workouts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/schedule-workouts/calendar/{username}", "testUser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].scheduledDateTime").value("2024-08-17"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].scheduledDateTime").value("2024-08-18"));
    }

    @Test
    @WithMockUser("testUser")
    public void testDeleteScheduledWorkout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/schedule-workouts/calendar/{username}/{eventId}", "testUser", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("testUser")
    public void testScheduleWorkout() throws Exception {
        WorkoutRequest workoutRequest = new WorkoutRequest();
        workoutRequest.setWorkoutDate("2024-08-17");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedule-workouts/{coachUsername}", "coachUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workoutDate\":\"2024-08-17\"}"))
                .andExpect(status().isOk());
    }
}
