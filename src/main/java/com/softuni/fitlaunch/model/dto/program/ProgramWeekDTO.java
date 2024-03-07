package com.softuni.fitlaunch.model.dto.program;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramWeekDTO {
    private ProgramDTO program;
    private List<ProgramWeekWorkoutDTO> weekWorkouts;
}
