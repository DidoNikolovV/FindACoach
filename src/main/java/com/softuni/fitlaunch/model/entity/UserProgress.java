package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_progress")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProgress extends BaseEntity {

    @Column(name = "exercise_completed")
    private boolean exerciseCompleted;

    @Column(name = "workout_started")
    private boolean workoutStarted;

    @Column(name = "workout_completed")
    private boolean workoutCompleted;

    @Column(name = "week_completed")
    private boolean weekCompleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private DayWorkoutsEntity workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private ProgramWeekEntity week;
}
