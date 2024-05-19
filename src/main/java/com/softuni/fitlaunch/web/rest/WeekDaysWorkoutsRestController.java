package com.softuni.fitlaunch.web.rest;

import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutAddDTO;
import com.softuni.fitlaunch.service.WorkoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/workouts")
public class WeekDaysWorkoutsRestController {

    private final WorkoutService workoutService;

    public WeekDaysWorkoutsRestController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("")
    public ResponseEntity<List<WorkoutAddDTO>> loadAllWorkouts() {
        return ResponseEntity.ok(workoutService.getAllWorkoutNames());
    }

    @PostMapping("/programs/{programId}/weeks/{weekNumber}")
    public ResponseEntity<List<DayWorkoutsDTO>> loadAllWorkouts(@PathVariable("programId") Long programId, @PathVariable("weekNumber") Long weekNumber, @RequestBody List<WorkoutAddDTO> workoutAddDTO) {
        List<DayWorkoutsDTO> allWorkoutsByIds = workoutService.getAllByWorkoutIds(programId, weekNumber, workoutAddDTO);
        return ResponseEntity.created(
                URI.create(String.format("/api/v1/workouts/programs/%d/weeks/%d", programId, weekNumber))
        ).body(allWorkoutsByIds);
    }
}
