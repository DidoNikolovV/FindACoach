package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;
}
