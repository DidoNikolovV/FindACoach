package com.softuni.fitlaunch.model.dto.user;

import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDTO {

    private Long id;

    private String username;
    @Email
    private String email;
    private String imgUrl;

    private Double weight;
    private Double height;
    private String targetGoals;

    private CoachDTO coach;

    private List<DayWorkoutsDTO> completedWorkouts;
}
