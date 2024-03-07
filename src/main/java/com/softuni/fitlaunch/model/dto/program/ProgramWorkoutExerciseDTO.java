package com.softuni.fitlaunch.model.dto.program;

import com.softuni.fitlaunch.model.dto.ExerciseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramWorkoutExerciseDTO {

    private ProgramWeekWorkoutDTO workout;

    private ExerciseDTO exercise;

    private int sets;

    private int reps;

    private boolean isCompleted;
}
