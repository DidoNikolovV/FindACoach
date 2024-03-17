package com.softuni.fitlaunch.model.dto.workout;

import jakarta.validation.constraints.NotNull;


public class WorkoutExerciseDTO {

    @NotNull
    private Long id;

    @NotNull
    private WorkoutDTO workout;
    @NotNull
    private com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO exercise;

    @NotNull
    private int sets;

    @NotNull
    private int reps;

    @NotNull
    private String videoUrl;

    @NotNull
    private boolean isCompleted;

    private Long programId;

    private Long weekId;

    public Long getId() {
        return id;
    }

    public WorkoutExerciseDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkoutDTO getWorkout() {
        return workout;
    }

    public WorkoutExerciseDTO setWorkout(WorkoutDTO workout) {
        this.workout = workout;
        return this;
    }

    public com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO getExercise() {
        return exercise;
    }

    public WorkoutExerciseDTO setExercise(com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO exercise) {
        this.exercise = exercise;
        return this;
    }

    public int getSets() {
        return sets;
    }

    public WorkoutExerciseDTO setSets(int sets) {
        this.sets = sets;
        return this;
    }

    public int getReps() {
        return reps;
    }

    public WorkoutExerciseDTO setReps(int reps) {
        this.reps = reps;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public WorkoutExerciseDTO setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public WorkoutExerciseDTO setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    public Long getProgramId() {
        return programId;
    }

    public WorkoutExerciseDTO setProgramId(Long programId) {
        this.programId = programId;
        return this;
    }

    public Long getWeekId() {
        return weekId;
    }

    public WorkoutExerciseDTO setWeekId(Long weekId) {
        this.weekId = weekId;
        return this;
    }
}
