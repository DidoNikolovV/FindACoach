package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "workout_exercises")
public class WorkoutExerciseEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(name = "video_url")
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;


    @NotNull
    private boolean isCompleted;

    @Column(nullable = false)
    private int sets;

    @Column(nullable = false)
    private int reps;

}
