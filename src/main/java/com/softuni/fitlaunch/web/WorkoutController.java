package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.service.WorkoutService;
import org.springframework.stereotype.Controller;

@Controller
public class WorkoutController {

    private final WorkoutService workoutService;


    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

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
