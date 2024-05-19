package com.softuni.fitlaunch.model.dto.program;

import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramWeekDTO {
    private int number;
    private List<DayWorkoutsDTO> days;
    private boolean isCompleted;
}
