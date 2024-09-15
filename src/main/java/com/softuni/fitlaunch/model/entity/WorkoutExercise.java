package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "program_workout_exercises")
public class WorkoutExercise extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "workout_id")
    private DayWorkoutsEntity workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
