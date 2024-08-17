package com.softuni.fitlaunch.integration;


import com.softuni.fitlaunch.model.dto.workout.WorkoutAddDTO;
import com.softuni.fitlaunch.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeekDaysWorkoutsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutService workoutService;


    @Test
    public void testLoadAllWorkoutsGet() throws Exception {
        List<WorkoutAddDTO> workoutAddDtoList = new ArrayList<>();
        WorkoutAddDTO workoutAddDto = new WorkoutAddDTO();
        workoutAddDto.setId(1L);
        workoutAddDto.setWorkoutName("Full Body");
        workoutAddDtoList.add(workoutAddDto);

        when(workoutService.getAllWorkoutNames()).thenReturn(workoutAddDtoList);

        mockMvc.perform(get("/api/v1/workouts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
