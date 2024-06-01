package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.service.ExerciseService;
import com.softuni.fitlaunch.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workout")
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

    @PostMapping("/create")
    public ResponseEntity<WorkoutDTO> createWorkout(@RequestParam("name") String name,
                                                    @RequestParam("level") String level,
                                                    @RequestParam("imgUrl") MultipartFile imgUrl,
                                                    @RequestParam("exercises") List<String> exercises,
                                                    Principal principal) {

        WorkoutDTO newWorkout = workoutService.createWorkout(name, level, imgUrl, principal.getName(), exercises);

        return ResponseEntity.created(
                URI.create("workouts/" + newWorkout.getId())
        ).body(newWorkout);
    }
}
