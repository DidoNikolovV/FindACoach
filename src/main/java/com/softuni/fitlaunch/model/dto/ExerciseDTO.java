package com.softuni.fitlaunch.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExerciseDTO {
    private Long id;
    private String name;
    @Pattern(regexp = "https:www\\.youtube\\.com/watch\\?v=.*", message = "Invalid youtube url provided")
    private String videoUrl;
    private String workout;
    private boolean isCompleted;
}
