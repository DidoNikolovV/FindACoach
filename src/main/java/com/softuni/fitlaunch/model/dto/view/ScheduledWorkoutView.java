package com.softuni.fitlaunch.model.dto.view;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduledWorkoutView {
    private Long id;
    private String clientName;
    private String coachName;
    private String scheduledDateTime;
}
