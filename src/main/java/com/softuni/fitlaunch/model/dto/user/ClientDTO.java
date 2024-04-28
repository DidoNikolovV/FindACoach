package com.softuni.fitlaunch.model.dto.user;

import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.model.entity.ScheduledWorkoutEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    private List<DailyWeightDTO> weight;
    private Double height;
    private String targetGoals;

    private CoachDTO coach;

    private List<DayWorkoutsDTO> completedWorkouts;

    private List<ScheduledWorkoutDTO> scheduledWorkouts;
}
