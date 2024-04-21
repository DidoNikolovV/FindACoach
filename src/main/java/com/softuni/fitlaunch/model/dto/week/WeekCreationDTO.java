package com.softuni.fitlaunch.model.dto.week;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeekCreationDTO {
    private int number;

    private List<String> days;
    private Long workoutId;
}
