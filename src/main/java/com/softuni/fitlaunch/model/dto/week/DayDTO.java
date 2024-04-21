package com.softuni.fitlaunch.model.dto.week;


import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DayDTO {
    private String name;
    private List<WorkoutDTO> workouts;
}
