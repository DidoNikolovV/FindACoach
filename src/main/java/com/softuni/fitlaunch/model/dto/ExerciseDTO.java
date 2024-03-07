package com.softuni.fitlaunch.model.dto;

import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ExerciseDTO {

    private Long id;

    private String name;

    @Pattern(regexp = "https:www\\.youtube\\.com/watch\\?v=.*", message = "Invalid youtube url provided")
    private String videoUrl;

    private List<WorkoutDTO> workouts;

    private boolean isCompleted;

    public ExerciseDTO() {
    }

    public ExerciseDTO(Long id, String name, String videoUrl, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.videoUrl = videoUrl;
        this.isCompleted = isCompleted;
    }

    public Long getId() {
        return id;
    }

    public ExerciseDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExerciseDTO setName(String name) {
        this.name = name;
        return this;
    }

    public List<WorkoutDTO> getWorkouts() {
        return workouts;
    }

    public ExerciseDTO setWorkouts(List<WorkoutDTO> workouts) {
        this.workouts = workouts;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public ExerciseDTO setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public ExerciseDTO setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }
}
