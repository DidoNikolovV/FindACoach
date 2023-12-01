package com.softuni.fitlaunch.model.dto;

import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ExerciseDTO {

    private Long id;

    private String name;

    private Integer sets;

    private Integer reps;

    @Pattern(regexp = "https:www\\.youtube\\.com/watch\\?v=.*", message = "Invalid youtube url provided")
    private String videoUrl;

    private List<WorkoutDTO> workouts;

    private boolean isCompleted;

    public ExerciseDTO() {
    }

    public ExerciseDTO(Long id, String name, Integer sets, Integer reps, String videoUrl, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
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

    public Integer getSets() {
        return sets;
    }

    public ExerciseDTO setSets(Integer sets) {
        this.sets = sets;
        return this;
    }

    public Integer getReps() {
        return reps;
    }

    public ExerciseDTO setReps(Integer reps) {
        this.reps = reps;
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
