package com.softuni.fitlaunch.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProgressDTO {
    private boolean workoutStarted;
    private boolean workoutCompleted;
    private Long workoutId;
    private String dayName;
}
