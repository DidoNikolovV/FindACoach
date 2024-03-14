package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.entity.WeekEntity;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.ProgramService;
import com.softuni.fitlaunch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProgramController {

    private final ProgramService programService;

    private final UserService userService;

    private final ClientService clientService;

    public ProgramController(ProgramService programService, UserService userService, ClientService clientService) {
        this.programService = programService;
        this.userService = userService;
        this.clientService = clientService;
    }

    @GetMapping("/programs/all")
    public String loadAllPrograms(Model model, Principal principal) {

        UserDTO userByUsername = userService.getUserByUsername(principal.getName());
        if (userByUsername.getTitle().equals(UserTitleEnum.CLIENT)) {
            ClientDTO clientDTO = clientService.getClientByUsername(userByUsername.getUsername());
            List<ProgramDTO> allPrograms = programService.loadAllProgramsByCoachId(clientDTO.getCoach().getId());
            model.addAttribute("allPrograms", allPrograms);
        }

        UserDTO loggedUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("membership", loggedUser.getMembership());

        return "programs";
    }

    @GetMapping("/programs/{programId}")
    public String loadProgramById(@PathVariable("programId") Long programId, Model model, Principal principal) {
        ProgramDTO programDTO = programService.getById(programId);

        ClientDTO clientDTO = clientService.getClientByUsername(principal.getName());

        List<WeekEntity> allWeeksByProgramId = programService.getAllWeeksByProgramId(programId);
        List<ProgramWeekWorkoutDTO> allProgramWorkouts = programService.getAllWorkoutsByProgramId(programId);

        model.addAttribute("program", programDTO);
        model.addAttribute("allWeeks", allWeeksByProgramId);
        model.addAttribute("allWorkouts", allWeeksByProgramId);
        model.addAttribute("allProgramWorkouts", allProgramWorkouts);
        model.addAttribute("user", clientDTO);


        return "program-details";
    }

    @GetMapping("/workouts/{programId}/{weekId}/{workoutId}")
    public String programWorkoutDetails(@PathVariable("programId") Long programId,
                                        @PathVariable("weekId") Long weekId,
                                        @PathVariable("workoutId") Long workoutId,
                                        Model model,
                                        Principal principal) {

        UserDTO loggedUser = userService.getUserByUsername(principal.getName());
        ProgramDTO program = programService.getById(programId);
        ProgramWeekDTO programWeekById = programService.getProgramWeekById(weekId);
        ProgramWeekWorkoutDTO programWeekWorkoutById = programService.getProgramWeekWorkoutById(workoutId, loggedUser);


        boolean hasStarted = userService.isWorkoutStarted(principal.getName(), programWeekWorkoutById);
        boolean isCompleted = userService.isWorkoutCompleted(principal.getName(), programWeekWorkoutById);
        boolean hasLiked = userService.isWorkoutLiked(loggedUser, programWeekWorkoutById);

        model.addAttribute("workout", programWeekWorkoutById);
        model.addAttribute("user", loggedUser);
        model.addAttribute("program", program);
        model.addAttribute("week", programWeekById);
        model.addAttribute("hasStarted", hasStarted);
        model.addAttribute("isCompleted", isCompleted);
        model.addAttribute("hasLiked", hasLiked);


        return "workout-details";
    }


    @PostMapping("/workouts/start/{programId}/{weekId}/{workoutId}")
    public String workoutStart(@PathVariable("programId") Long programId, @PathVariable("weekId") Long weekId, @PathVariable("workoutId") Long workoutId, Principal principal) {
        userService.startProgramWorkout(principal.getName(), workoutId);
        return String.format("redirect:/workouts/%d/%d/%d", programId, weekId, workoutId);
    }


//    @PostMapping("/workouts/complete/{programId}/{weekId}/{workoutId}")
//    public String workoutComplete(@PathVariable("programId") Long programId, @PathVariable("weekId") Long weekId, @PathVariable("workoutId") Long workoutId, Principal principal) {
//
//        clientService.completedProgramWorkout(principal.getName(), programId);
//        return String.format("redirect:/workouts/%d/%d/%d", programId, weekId, workoutId);
//    }

    @PostMapping("/workouts/like/{programId}/{weekId}/{workoutId}")
    public String like(@PathVariable("programId") Long programId, @PathVariable("weekId") Long weekId, @PathVariable("workoutId") Long workoutId,
                          Principal principal) {

        UserDTO loggedUser = userService.getUserByUsername(principal.getName());
        userService.like(loggedUser, workoutId);

        return String.format("redirect:/workouts/%d/%d/%d", programId, weekId, workoutId);
    }

}
