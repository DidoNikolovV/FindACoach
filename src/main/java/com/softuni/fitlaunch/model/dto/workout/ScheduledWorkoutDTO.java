package com.softuni.fitlaunch.model.dto.workout;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduledWorkoutDTO {
    private Long id;
    private Long clientId;
    private Long coachId;
    private LocalDate scheduledDate;
}
