package com.softuni.fitlaunch.model.dto.workout;


import com.softuni.fitlaunch.model.dto.ExerciseDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ClientWorkoutDetails {
    private Long id;
    private String name;
}
