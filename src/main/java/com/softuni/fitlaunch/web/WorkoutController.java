package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.enums.LevelEnum;
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

    private final UserService userService;

    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public String all(Model model,
                      @PageableDefault(
                              size = 6,
                              sort = "id"
                      ) Pageable pageable) {

        Page<WorkoutDTO> allWorkouts = workoutService.getAllWorkouts(pageable);
        List<LevelEnum> allLevels = workoutService.getAllLevels();

        model.addAttribute("workouts", allWorkouts);
        model.addAttribute("levels", allLevels);

        return "workouts";
    }

    @PostMapping("/{workoutId}/weeks/{weekId}/days/{dayName}/like")
    public String like(@PathVariable("workoutId") Long workoutId, @PathVariable("dayName") String dayName, @PathVariable("weekId") Long weekNumber, Principal principal) {
        workoutService.like(workoutId, principal.getName());

        return String.format("redirect:/workouts/%d/weeks/%d/days/%s", workoutId, weekNumber, dayName);
    }

    @PostMapping("/{workoutId}/weeks/{weekId}/days/{dayName}/dislike")
    public String dislike(@PathVariable("workoutId") Long workoutId, @PathVariable("dayName") String dayName, @PathVariable("weekId") Long weekNumber, Principal principal) {
        workoutService.dislike(workoutId, principal.getName());

        return String.format("redirect:/workouts/%d/weeks/%d/days/%s", workoutId, weekNumber, dayName);
    }

    @PostMapping("/{workoutId}/weeks/{weekId}/days/{dayName}/exercise/{exerciseId}/complete")
    public String dislike(@PathVariable("workoutId") Long workoutId, @PathVariable("dayName") String dayName,
                          @PathVariable("exerciseId") Long exerciseId,
                          @PathVariable("weekId") Long weekNumber, Principal principal) {

        workoutService.completeExercise(workoutId, dayName, exerciseId);
        return String.format("redirect:/workouts/%d/weeks/%d/days/%s", workoutId, weekNumber, dayName);
    }

    @GetMapping("/create")
    public String createWorkout(Model model) {
        model.addAttribute("workoutCreationDTO", new WorkoutCreationDTO());
        model.addAttribute("activePage", "createWorkout");

        return "workout-add";
    }

    @PostMapping("/create")
    public String createWorkout(@ModelAttribute("workoutCreationDTO") WorkoutCreationDTO workoutCreationDTO, Principal principal) {

        WorkoutDTO workout = workoutService.createWorkout(workoutCreationDTO, principal.getName());

        return "redirect:/workouts/" + workout.getId();
    }

    @GetMapping("/{workoutId}")
    public String loadWorkoutDetails(@PathVariable("workoutId") Long workoutId, Model model) {

        WorkoutDTO workout = workoutService.getWorkoutById(workoutId);

        model.addAttribute("workout", workout);

        return "workout";
    }

    @PostMapping("/{workoutId}/weeks/{weekId}/days/{dayName}/start")
    public String startWorkout(@PathVariable("workoutId") Long workoutId, @PathVariable("weekId") Long weekNumber, @PathVariable("dayName") String dayName, Principal principal) {

        workoutService.startWorkout(workoutId, principal.getName(), weekNumber, dayName);

        return String.format("redirect:/workouts/%d/weeks/%d/days/%s", workoutId, weekNumber, dayName);
    }

    @PostMapping("/{workoutId}/weeks/{weekId}/days/{dayName}/complete")
    public String completeWorkout(@PathVariable("workoutId") Long workoutId, @PathVariable("weekId") Long weekNumber, @PathVariable("dayName") String dayName, Principal principal) {

        workoutService.completedWorkout(workoutId, principal.getName(), weekNumber, dayName);

        return String.format("redirect:/workouts/%d/weeks/%d/days/%s", workoutId, weekNumber, dayName);
    }

    @GetMapping("{workoutId}/weeks/{weekId}/days/{dayName}")
    public String workoutDetails(@PathVariable("workoutId") Long workoutId, @PathVariable("weekId") Long weekNumber, @PathVariable("dayName") String dayName, Model model, Principal principal) {

        WorkoutDetailsDTO workoutDetailsDTO = workoutService.getWorkoutDetailsById(workoutId, dayName);

        boolean hasStarted = workoutService.isWorkoutStarted(workoutId, weekNumber, principal.getName());
        boolean isCompleted = workoutService.isWorkoutCompleted(workoutId, weekNumber, dayName, principal.getName());
        boolean hasLiked = userService.isWorkoutLiked(workoutId, principal.getName());

        model.addAttribute("workout", workoutDetailsDTO);
        model.addAttribute("hasStarted", hasStarted);
        model.addAttribute("isCompleted", isCompleted);
        model.addAttribute("hasLiked", hasLiked);

        return "workout-details";
    }
}
