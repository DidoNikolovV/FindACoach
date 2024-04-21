package com.softuni.fitlaunch.model.dto.program;

import com.softuni.fitlaunch.model.dto.week.DayDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramWeekDTO {
    private int number;
    private List<DayDTO> days;
    private ProgramDTO program;
}
