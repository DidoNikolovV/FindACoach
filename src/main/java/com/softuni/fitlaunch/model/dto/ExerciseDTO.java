package com.softuni.fitlaunch.model.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {

    private Long id;
    private String name;
    private String targetMuscleGroup;
    private String videoUrl;
}
