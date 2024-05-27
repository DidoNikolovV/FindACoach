package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.service.ExerciseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercises")
public class WorkoutRestController {

    private final ExerciseService exerciseService;

    public WorkoutRestController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public List<WorkoutExerciseDTO> loadExercises() {
        return exerciseService.loadAllExercises();
    }

//    @PostMapping("/create")
//    public WorkoutDTO createWorkout() {
//
//    }
}
