package com.softuni.fitlaunch.model.dto.week;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeekDTO {

    private int number;

    private List<DayDTO> days;
}
