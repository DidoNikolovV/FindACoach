package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "program_workouts_exercises")
@Getter
@Setter
public class ProgramWorkoutExerciseEntity extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "workout_id")
    private ProgramWeekWorkoutEntity workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    @Column(columnDefinition = "int default 0")
    private int sets;

    @Column(columnDefinition = "int default 0")
    private int reps;

    @Column(name = "is_completed")
    private boolean isCompleted;


}
