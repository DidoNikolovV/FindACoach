package com.softuni.fitlaunch.model.dto.workout;

import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateWorkoutDTO {

    @NotBlank(message = "Name should not be null nor empty")
    @Length(max = 255, message = "Name cannot be more than 255 characters long")
    @Pattern(regexp = "^(?!\\s)[a-zA-Z0-9-* ]+", message = "Invalid workout name")
    private String name;

    @NotNull
    private LevelEnum level;

    @NotBlank(message = "Description should not be null nor empty")
    @Length(max = 255, message = "Description cannot be more than 255 characters long")
    private String description;

    @NotNull(message = "Image should not be null")
    private MultipartFile imgUrl;

    @NotNull
    private List<WorkoutExerciseDTO> exercises;

    private List<Long> selectedExerciseIds;

    private List<Integer> sets;
    private List<Integer> reps;
}
