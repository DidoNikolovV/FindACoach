package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "days_workouts")
@Getter
@Setter
public class DayWorkoutsEntity extends BaseEntity{

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    @Column(name = "is_completed")
    private boolean isCompleted = false;

    @Column(name = "is_started")
    private boolean isStarted = false;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private ProgramWeekEntity week;

}
