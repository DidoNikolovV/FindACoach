package com.softuni.fitlaunch.model.dto.user;

import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
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

    private Double weight;

    private Double weightGoal;

    private String performanceGoals;

    private String bodyCompositionGoal;

    private String imgUrl;

    private CoachDTO coach;

    private List<DayWorkoutsDTO> completedWorkouts;

    private List<ScheduledWorkoutDTO> scheduledWorkouts;

    private List<DailyMetricsDTO> dailyMetrics;
}
