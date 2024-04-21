package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "days")
@Getter
@Setter
public class DayEntity extends BaseEntity {
    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private WeekEntity week;

    @ManyToMany
    @JoinTable(
            name = "workouts_days",
            joinColumns = @JoinColumn(name = "day_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id"))
    private List<WorkoutEntity> workouts = new ArrayList<>();
}
