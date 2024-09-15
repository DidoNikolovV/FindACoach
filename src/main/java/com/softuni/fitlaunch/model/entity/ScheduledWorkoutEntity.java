package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "scheduled_workouts")
public class ScheduledWorkoutEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private CoachEntity coach;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    private LocalDate scheduledDate;
}
