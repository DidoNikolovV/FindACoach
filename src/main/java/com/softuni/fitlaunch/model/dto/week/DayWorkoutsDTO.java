package com.softuni.fitlaunch.model.dto.week;

import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DayWorkoutsDTO {
    private int id;
    private String name;
    private WorkoutDTO workout;
    private boolean isCompleted; // Derived from UserProgressDTO
    private boolean isStarted;
}
