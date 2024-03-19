package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.ProgramService;
import com.softuni.fitlaunch.service.UserService;
import com.softuni.fitlaunch.service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    private final ProgramService programService;

    private final UserService userService;

    private final ClientService clientService;


    public WorkoutController(WorkoutService workoutService, ProgramService programService, UserService userService, ClientService clientService) {
        this.workoutService = workoutService;
        this.programService = programService;
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

    @GetMapping("/{programId}/{weekId}/{workoutId}")
    public String programWorkoutDetails(@PathVariable("programId") Long programId,
                                        @PathVariable("weekId") Long weekId,
                                        @PathVariable("workoutId") Long workoutId,
                                        Model model,
                                        Principal principal) {

        UserDTO loggedUser = userService.getUserByUsername(principal.getName());
        WorkoutDTO workout = workoutService.getWorkoutById(workoutId);
        ProgramDTO program = programService.getById(programId);
//        WorkoutDTO programWeekById = programService.getProgramWeekById(weekId);
//        WorkoutDTO programWeekWorkoutById = programService.getProgramWeekWorkoutById(workoutId, loggedUser);


        boolean hasStarted = clientService.isWorkoutStarted(principal.getName(), workoutId);
        boolean isCompleted = clientService.isWorkoutCompleted(principal.getName(), workoutId);
        boolean hasLiked = userService.isWorkoutLiked(loggedUser, workout);

        model.addAttribute("workout", workout);
        model.addAttribute("user", loggedUser);
        model.addAttribute("program", program);
//        model.addAttribute("week", programWeekById);
        model.addAttribute("hasStarted", hasStarted);
        model.addAttribute("isCompleted", isCompleted);
        model.addAttribute("hasLiked", hasLiked);


        return "workout-details";
    }

    @PostMapping("/like/{programId}/{weekId}/{workoutId}")
    public String like(@PathVariable("programId") Long programId, @PathVariable("weekId") Long weekId, @PathVariable("workoutId") Long workoutId,
                       Principal principal) {

        UserDTO loggedUser = userService.getUserByUsername(principal.getName());
        userService.like(loggedUser, workoutId);

        return String.format("redirect:/workouts/%d/%d/%d", programId, weekId, workoutId);
    }


//    @PostMapping("/workouts/start/{programId}/{weekId}/{workoutId}")
//    public String workoutStart(@PathVariable("programId") Long programId, @PathVariable("weekId") Long weekId, @PathVariable("workoutId") Long workoutId, Principal principal) {
//        workoutService.startWorkout(programId, workoutId);
//        return String.format("redirect:/workouts/%d/%d/%d", programId, weekId, workoutId);
//    }

//    @GetMapping("/workouts/add")
//    public String add(Model model) {
//
//        if (!model.containsAttribute("createWorkoutDTO")) {
//            model.addAttribute("createWorkoutDTO", new CreateWorkoutDTO());
//        }
//
//        List<ExerciseDTO> allExercises = workoutService.getAllExercises();
//
//        model.addAttribute("exercises", allExercises);
//
//        return "workout-add";
//    }

//    @GetMapping("/workouts/history")
//    public String workoutHistory(Model model, Principal principal) {
//        ClientDTO clientByUsername = clientService.getClientByUsername(principal.getName());
//        List<ProgramWeekWorkoutDTO> completedWorkouts = clientByUsername.getCompletedWorkouts();
//
//        model.addAttribute("completedWorkouts", completedWorkouts);
//
//        return "workouts-log";
//    }

//    @PostMapping("/workouts/add")
//    public String add(@ModelAttribute CreateWorkoutDTO createWorkoutDTO, Principal principal,
//                      BindingResult bindingResult,
//                      RedirectAttributes rAtt) throws IOException {
//
//        if(bindingResult.hasErrors()) {
//            rAtt.addFlashAttribute("createWorkoutDTO", createWorkoutDTO);
//            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.createWorkoutDTO", createWorkoutDTO);
//            return "redirect:/workout/add";
//        }
//
//
//        String imageUrl = fileUpload.uploadFile(createWorkoutDTO.getImgUrl());
//        WorkoutEntity workout = new WorkoutEntity();
//        workout
//                .setAuthor(userService.getUserByUsername(principal.getName()))
//                .setName(createWorkoutDTO.getName())
//                .setLevel(createWorkoutDTO.getLevel())
//                .setDescription(createWorkoutDTO.getDescription())
//                .setImgUrl(imageUrl);
//
//
//        workoutService.createWorkout(workout);
//
//        List<Integer> sets = createWorkoutDTO.getSets();
//        List<Integer> reps = createWorkoutDTO.getReps();
//
//
//        List<Long> selectedExercisesIds = createWorkoutDTO.getSelectedExerciseIds();
//        List<ExerciseEntity> selectedExercises = exerciseService.getExercisesByIds(selectedExercisesIds);
//
//        for (ExerciseEntity selectedExercise : selectedExercises) {
//            int selectedExerciseId = Integer.parseInt(String.valueOf(selectedExercisesIds.get(selectedExercisesIds.indexOf(selectedExercise.getId()))));
//
//            Integer selectedExerciseSets = sets.get(selectedExerciseId - 1);
//            Integer selectedExerciseReps = reps.get(selectedExerciseId - 1);
//
//            WorkoutExerciseEntity exercise = new WorkoutExerciseEntity()
//                    .setWorkout(workout)
//                    .setExercise(selectedExercise)
//                    .setSets(selectedExerciseSets)
//                    .setReps(selectedExerciseReps)
//                    .setVideoUrl(selectedExercise.getVideoUrl());
//
//            workoutExerciseService.saveWorkoutExercise(exercise);
//        }
//
//        long newWorkoutID =  workout.getId();
//
//        return "redirect:/workouts/" + newWorkoutID;
//    }
//
//
//
//    @GetMapping("/workouts/{id}")
//    public String details(@PathVariable("id") Long id, Model model, Principal principal) {
//
//        UserDTO currentLoggedUser = userService.getUserByUsername(principal.getName());
//
//        WorkoutDetailsDTO workout = workoutService.getWorkoutDetails(id).orElseThrow(() -> new ObjectNotFoundException("Workout with id " + id + " not found!" ));;
//        List<WorkoutExerciseEntity> allWorkoutExercises = workoutExerciseService.getAllWorkoutExercisesByWorkoutId(workout.getId());
//
//        boolean hasLiked = false;
//        boolean isCompleted = false;
//        boolean hasStarted = false;
//
//
//        for (ProgramWeekWorkoutEntity workoutEntity : currentLoggedUser.getWorkoutsStarted()) {
//            if(workoutEntity.getId().equals(workout.getId())) {
//                hasStarted = true;
//                break;
//            }
//        }
//
//        for (ProgramWeekWorkoutEntity workoutEntity : currentLoggedUser.getWorkoutsCompleted()) {
//            if(workoutEntity.getId().equals(workout.getId())) {
//                isCompleted = true;
//            }
//        }
//
//        for (UserDTO userDTO : workout.getUsersLiked()) {
//            if(userDTO.getUsername().equals(principal.getName())) {
//                hasLiked = true;
//                break;
//            }
//        }
//
//
//
//        model.addAttribute("workout", workout);
//        model.addAttribute("allWorkoutExercises", allWorkoutExercises);
//        model.addAttribute("hasLiked", hasLiked);
//        model.addAttribute("hasStarted", hasStarted);
//        model.addAttribute("isCompleted", isCompleted);
//
//        return "workout-details";
//    }


//    @PostMapping("/workouts/{id}")
//    public String details(@PathVariable("id") Long id,
//                          Principal principal) {
//
//        String currentUserUsername = principal.getName();
//        WorkoutDetailsDTO workoutDetails = workoutService.getWorkoutDetails(id).orElseThrow(() -> new RuntimeException("Workout not found"));
//
//
//        boolean hasLiked = false;
//
//        for (UserDTO userDTO : workoutDetails.getUsersLiked()) {
//            if(userDTO.getUsername().equals(currentUserUsername)) {
//                hasLiked = true;
//                break;
//            }
//        }
//
//        if(hasLiked) {
//            userService.dislike(currentUserUsername, workoutDetails.getId());
//        } else {
//            userService.like(currentUserUsername, workoutDetails.getId());
//        }
//
//
//        return "redirect:/workouts/" + id;
//    }

//    @PostMapping("/workouts/start/{id}")
//    public String workoutStart(@PathVariable("id") Long id, Principal principal) {
//
//        workoutService.startWorkout(id, principal.getName());
//
//        return "redirect:/workouts/" + id;
//    }

//    @PostMapping("/workouts/complete/{id}")
//    public String workoutComplete(@PathVariable("id") Long id, Principal principal) {
//
//        WorkoutDetailsDTO workoutDetails = workoutService.getWorkoutDetails(id).orElseThrow(() -> new RuntimeException("Workout not found"));
//        String currentUserUsername = principal.getName();
//
//
//        workoutService.completeWorkout(workoutDetails.getId(), currentUserUsername);
//
//        return "redirect:/workouts/" + id;
//    }

//    @PostMapping("/workouts/{workoutId}/complete/{exerciseId}")
//    public String exerciseComplete(@PathVariable("workoutId") Long workoutId, @PathVariable("exerciseId") Long exerciseId) {
//        workoutService.completeExercise(workoutId, exerciseId);
//
//        return "redirect:/workouts/" + workoutId;
//    }

}
