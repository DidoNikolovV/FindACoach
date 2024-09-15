package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "days_workouts")
public class DayWorkoutsEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private ProgramWeekEntity week;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProgress> userProgress = new ArrayList<>();

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<WorkoutExercise> exercises;
}
