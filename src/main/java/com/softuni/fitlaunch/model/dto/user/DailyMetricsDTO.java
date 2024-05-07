package com.softuni.fitlaunch.model.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMetricsDTO {
    private Double weight;
    private Double caloriesIntake;
    private Double stepsCount;
    private Double sleepDuration;
    private Integer mood;
    private Integer energyLevels;
    private LocalDate date;
}