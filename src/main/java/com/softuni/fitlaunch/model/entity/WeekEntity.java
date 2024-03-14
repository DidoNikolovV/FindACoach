package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "weeks")
@Getter
@Setter
public class WeekEntity extends BaseEntity {

    @OneToMany
    private List<WorkoutEntity> workouts;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private ProgramEntity program;
}
