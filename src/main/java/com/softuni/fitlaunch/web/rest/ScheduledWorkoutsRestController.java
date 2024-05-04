package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.view.ScheduledWorkoutView;
import com.softuni.fitlaunch.model.dto.workout.WorkoutRequest;
import com.softuni.fitlaunch.service.ScheduleWorkoutService;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/schedule-workouts")
public class ScheduledWorkoutsRestController {

    private final ScheduleWorkoutService scheduleWorkoutService;

    public ScheduledWorkoutsRestController(ScheduleWorkoutService scheduleWorkoutService) {
        this.scheduleWorkoutService = scheduleWorkoutService;
    }

    @GetMapping("/calendar/{username}")
    public ResponseEntity<List<ScheduledWorkoutView>> getAllScheduledWorkouts(@PathVariable("username") String username) {
        List<ScheduledWorkoutView> allScheduledWorkouts = scheduleWorkoutService.getAllScheduledWorkouts(username);
        return ResponseEntity.ok(allScheduledWorkouts);
    }

    @DeleteMapping("/calendar/{username}/{eventId}")
    public ResponseEntity<List<ScheduledWorkoutView>> deleteScheduledWorkout(@PathVariable("username") String username, @PathVariable("eventId") Long eventId) throws ResourceNotFoundException {
        scheduleWorkoutService.deleteScheduledWorkout(eventId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{coachUsername}")
    public ResponseEntity<Void> scheduleWorkout(@PathVariable("coachUsername") String coachUsername, @RequestBody WorkoutRequest workoutRequest,
                                                Principal principal) {
        String scheduledTime = workoutRequest.getWorkoutDate();
        scheduleWorkoutService.scheduleWorkout(principal.getName(), coachUsername, scheduledTime);
        return ResponseEntity.ok().build();
    }
}
