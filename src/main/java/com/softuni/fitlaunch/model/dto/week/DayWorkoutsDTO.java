package com.softuni.fitlaunch.model.dto.week;

import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DayWorkoutsDTO {
    private Long id;
    private String name;
    private WorkoutDTO workout;
    private boolean isCompleted;
    private boolean isStarted;
}
