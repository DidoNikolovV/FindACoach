package com.softuni.fitlaunch.model.dto.program;

import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramWorkoutExerciseDTO {

    private ProgramWeekWorkoutDTO workout;

    private WorkoutExerciseDTO exercise;

    private int sets;

    private int reps;

    private boolean isCompleted;
}
