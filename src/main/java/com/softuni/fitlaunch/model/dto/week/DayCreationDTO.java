package com.softuni.fitlaunch.model.dto.week;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DayCreationDTO {

    private List<String> name;
    private Long workoutId;
}
