package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.ExerciseService;
import com.softuni.fitlaunch.service.ProgramService;
import com.softuni.fitlaunch.service.UserService;
import com.softuni.fitlaunch.service.WorkoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    private final ProgramService programService;

    private final ExerciseService exerciseService;

    private final UserService userService;

    private final ClientService clientService;


    public WorkoutController(WorkoutService workoutService, ProgramService programService, ExerciseService exerciseService, UserService userService, ClientService clientService) {
        this.workoutService = workoutService;
        this.programService = programService;
        this.exerciseService = exerciseService;
        this.userService = userService;
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public String all(Model model,
                      @PageableDefault(
                              size = 3,
                              sort = "id"
                      ) Pageable pageable) {

        Page<WorkoutDTO> allWorkouts = workoutService.getAllWorkouts(pageable);
        List<LevelEnum> allLevels = workoutService.getAllLevels();

        model.addAttribute("workouts", allWorkouts);
        model.addAttribute("levels", allLevels);

        return "workouts";
    }


    @PostMapping("/like/{workoutId}")
    public String like(@PathVariable("workoutId") Long workoutId, Principal principal) {
        userService.like(workoutId, principal.getName());

        return "redirect:/workouts/" + workoutId;
    }

    @PostMapping("/dislike/{workoutId}")
    public String dislike(@PathVariable("workoutId") Long workoutId, Principal principal) {
        userService.dislike(workoutId, principal.getName());

        return "redirect:/workouts/" + workoutId;
    }

    @GetMapping("/create")
    public String createWorkout(Model model) {

        List<WorkoutExerciseDTO> exercises = exerciseService.loadAllExercises();

        model.addAttribute("exercises", exercises);
        model.addAttribute("workoutCreationDTO", new WorkoutCreationDTO());

        return "workout-add";
    }

    @PostMapping("/create")
    public String createWorkout(@ModelAttribute("workoutCreationDTO") WorkoutCreationDTO workoutCreationDTO, Principal principal) {

        WorkoutDTO workout = workoutService.createWorkout(workoutCreationDTO, principal.getName());

        return "redirect:/workouts/" + workout.getId();
    }

    @PostMapping("/{workoutId}/start")
    public String startWorkout(@PathVariable("workoutId") Long workoutId, Principal principal) {

        workoutService.startWorkout(workoutId, principal.getName());

        return "redirect:/workouts/" + workoutId;
    }

    @PostMapping("/{workoutId}/complete")
    public String completeWorkout(@PathVariable("workoutId") Long workoutId, Principal principal) {

        workoutService.completedWorkout(workoutId, principal.getName());

        return "redirect:/workouts/" + workoutId;
    }

    @GetMapping("{workoutId}")
    public String workoutDetails(@PathVariable("workoutId") Long workoutId, Model model, Principal principal) {

        WorkoutDetailsDTO workoutDetailsDTO = workoutService.getWorkoutDetailsById(workoutId);

        boolean hasStarted = workoutService.isWorkoutStarted(workoutId, principal.getName());
        boolean isCompleted = workoutService.isWorkoutCompleted(workoutId, principal.getName());
        boolean hasLiked = userService.isWorkoutLiked(workoutId, principal.getName());

        model.addAttribute("workout", workoutDetailsDTO);
        model.addAttribute("hasStarted", hasStarted);
        model.addAttribute("isCompleted", isCompleted);
        model.addAttribute("hasLiked", hasLiked);

        return "workout-details";
    }
}
