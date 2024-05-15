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
    private Long id;
    private String name;
    private Long workoutId;
    private Long weekId;
    private boolean isCompleted;
    private boolean isStarted;
}
