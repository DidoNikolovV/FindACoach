package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.WeekEntity;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.ExerciseService;
import com.softuni.fitlaunch.service.ProgramService;
import com.softuni.fitlaunch.service.UserService;
import com.softuni.fitlaunch.service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/programs")
public class ProgramController {

    private final ProgramService programService;

    private final UserService userService;

    private final ClientService clientService;

    private final ExerciseService exerciseService;


    public ProgramController(ProgramService programService, UserService userService, ClientService clientService, ExerciseService exerciseService) {
        this.programService = programService;
        this.userService = userService;
        this.clientService = clientService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/all")
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

    @GetMapping("/{programId}")
    public String loadProgramById(@PathVariable("programId") Long programId, Model model, Principal principal) {
        ProgramDTO programDTO = programService.getById(programId);

        ClientDTO clientDTO = clientService.getClientByUsername(principal.getName());

        List<WeekEntity> allWeeksByProgramId = programService.getAllWeeksByProgramId(programId);
        List<WorkoutDTO> allProgramWorkouts = programService.getAllWorkoutsByProgramId(programId);

        model.addAttribute("program", programDTO);
        model.addAttribute("allWeeks", allWeeksByProgramId);
        model.addAttribute("allWorkouts", allWeeksByProgramId);
        model.addAttribute("allProgramWorkouts", allProgramWorkouts);
        model.addAttribute("user", clientDTO);


        return "program-details";
    }

    @GetMapping("/create")
    public String loadProgramCreation(Model model, Principal principal) {

        model.addAttribute("allExercises", exerciseService.loadAllExercises());

        return "create-program";
    }
}
