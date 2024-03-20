package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.view.ScheduledWorkoutView;
import com.softuni.fitlaunch.service.ScheduleWorkoutService;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class ScheduledWorkoutsRestController {

    private final ScheduleWorkoutService scheduleWorkoutService;


    public ScheduledWorkoutsRestController(ScheduleWorkoutService scheduleWorkoutService) {
        this.scheduleWorkoutService = scheduleWorkoutService;
    }

    @GetMapping("/{username}/calendar/scheduledWorkouts")
    public ResponseEntity<List<ScheduledWorkoutView>> getAllScheduledWorkouts(@PathVariable("username") String username) {
        List<ScheduledWorkoutView> allScheduledWorkouts = scheduleWorkoutService.getAllScheduledWorkouts(username);
        return ResponseEntity.ok(allScheduledWorkouts);
    }


    @DeleteMapping("/{username}/calendar/scheduledWorkouts/{eventId}")
    public ResponseEntity<List<ScheduledWorkoutView>> deleteScheduledWorkout(@PathVariable("username") String username, @PathVariable("eventId") Long eventId) throws ObjectNotFoundException {
        scheduleWorkoutService.deleteScheduledWorkout(eventId);
        return ResponseEntity.noContent().build();
    }
}
