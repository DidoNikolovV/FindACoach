package com.softuni.fitlaunch.model.dto;

import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExerciseDTO {
    private Long id;
    private String name;
    @Pattern(regexp = "https:www\\.youtube\\.com/watch\\?v=.*", message = "Invalid youtube url provided")
    private String videoUrl;
    private List<WorkoutDTO> workouts;
    private boolean isCompleted;
}
