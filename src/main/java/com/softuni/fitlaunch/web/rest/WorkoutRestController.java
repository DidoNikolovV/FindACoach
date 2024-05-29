package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.service.ExerciseService;
import com.softuni.fitlaunch.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exercises")
public class WorkoutRestController {

    private final ExerciseService exerciseService;
    private final WorkoutService workoutService;

    public WorkoutRestController(ExerciseService exerciseService, WorkoutService workoutService) {
        this.exerciseService = exerciseService;
        this.workoutService = workoutService;
    }

    @GetMapping
    public List<WorkoutExerciseDTO> loadExercises() {
        return exerciseService.loadAllExercises();
    }

    @PostMapping("/add")
    public ResponseEntity<WorkoutDTO> createWorkout(List<WorkoutExerciseDTO> exercises) {
//        WorkoutDTO workoutDTO = workoutService.addExercisesToWorkout(workoutId, exercises);

        return (ResponseEntity<WorkoutDTO>) ResponseEntity.ok();
    }
}
