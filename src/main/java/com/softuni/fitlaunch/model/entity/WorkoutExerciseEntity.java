package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "workout_exercises")
public class WorkoutExerciseEntity extends BaseEntity {

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
}
