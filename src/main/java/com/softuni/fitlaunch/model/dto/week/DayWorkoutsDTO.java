package com.softuni.fitlaunch.model.dto.week;

import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DayWorkoutsDTO {
    private String name;
    private WorkoutDTO workout;
    private boolean isCompleted; // Derived from UserProgressDTO
    private boolean isStarted;
}
