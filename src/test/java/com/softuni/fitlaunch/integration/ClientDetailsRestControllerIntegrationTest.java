package com.softuni.fitlaunch.integration;


import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.dto.workout.ClientWorkoutDetails;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientDetailsRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private WorkoutService workoutService;

    @Test
    void testLoadCompletedWorkoutDetails() throws Exception {
        String clientName = "testClient";
        Long workoutId = 1L;
        String dayName = "Monday";

        List<ClientWorkoutDetails> workoutDetails = List.of(new ClientWorkoutDetails());
        when(workoutService.getCompletedWorkoutsForClient(anyString())).thenReturn(workoutDetails);

        mockMvc.perform(get("/api/v1/clients/completed-workouts/{clientName}/workouts/{workoutId}/{dayName}/details", clientName, workoutId, dayName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{}]")); // Adjust based on actual JSON structure
    }

    @Test
    void testLoadMetricsData() throws Exception {
        String clientName = "testClient";

        List<DailyMetricsDTO> metrics = List.of(new DailyMetricsDTO());
        when(clientService.calculateAverageWeeklyMetrics(anyString())).thenReturn(metrics);

        mockMvc.perform(get("/api/v1/clients/{clientName}/metrics/data", clientName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{}]")); // Adjust based on actual JSON structure
    }

    @Test
    void testLoadWeekMetricsData() throws Exception {
        String clientName = "testClient";
        int weekNumber = 1;

        List<DailyMetricsDTO> metrics = List.of(new DailyMetricsDTO());
        when(clientService.getAllByWeekNumber(anyInt(), anyString())).thenReturn(metrics);

        mockMvc.perform(get("/api/v1/clients/{clientName}/metrics/week/{weekNumber}", clientName, weekNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{}]")); // Adjust based on actual JSON structure
    }
}
