package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "daily_metrics")
@Getter
@Setter
public class DailyMetricsEntity extends BaseEntity {

    @Column(name = "weight")
    private Double weight;
    @Column(name = "calories_intake")
    private Double caloriesIntake;
    @Column(name = "steps_count")
    private Double stepsCount;
    @Column(name = "sleep_duration")
    private Double sleepDuration;
    @Column(name = "mood")
    private Integer mood;
    @Column(name = "energy_levels")
    private Integer energyLevels;
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private WeekMetricsEntity week;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;
}
