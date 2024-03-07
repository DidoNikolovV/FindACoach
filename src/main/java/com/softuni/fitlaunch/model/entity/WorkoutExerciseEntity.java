package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "workout_exercises")
public class WorkoutExerciseEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;


    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    private int sets;
    private int reps;

    @Column(name = "video_url")
    private String videoUrl;


    @Column(nullable = false)
    private boolean isCompleted;



    public WorkoutEntity getWorkout() {
        return workout;
    }

    public WorkoutExerciseEntity setWorkout(WorkoutEntity workout) {
        this.workout = workout;
        return this;
    }

    public ExerciseEntity getExercise() {
        return exercise;
    }

    public WorkoutExerciseEntity setExercise(ExerciseEntity exercise) {
        this.exercise = exercise;
        return this;
    }

    public int getSets() {
        return sets;
    }

    public WorkoutExerciseEntity setSets(int sets) {
        this.sets = sets;
        return this;
    }

    public int getReps() {
        return reps;
    }

    public WorkoutExerciseEntity setReps(int reps) {
        this.reps = reps;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public WorkoutExerciseEntity setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public WorkoutExerciseEntity setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

}
