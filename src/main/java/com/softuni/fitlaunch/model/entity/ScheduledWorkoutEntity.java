package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "scheduled_workouts")
public class ScheduledWorkoutEntity extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private CoachEntity coach;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;



    private LocalDateTime scheduledDateTime;
}
