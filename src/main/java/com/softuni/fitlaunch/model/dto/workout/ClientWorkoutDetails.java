package com.softuni.fitlaunch.model.dto.workout;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ClientWorkoutDetails {
    private Long id;
    private String name;
    private String workoutName;
}
