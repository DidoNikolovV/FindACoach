package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.program.ProgramCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.week.DayCreationDTO;
import com.softuni.fitlaunch.model.dto.week.WeekCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.WeekEntity;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.ProgramService;
import com.softuni.fitlaunch.service.UserService;
import com.softuni.fitlaunch.service.WeekService;
import com.softuni.fitlaunch.service.WorkoutService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/programs")
public class ProgramController {

    private final ProgramService programService;

    private final UserService userService;

    private final ClientService clientService;

    private final WeekService weekService;

    private final WorkoutService workoutService;


    public ProgramController(ProgramService programService, UserService userService, ClientService clientService, WeekService weekService, WorkoutService workoutService) {
        this.programService = programService;
        this.userService = userService;
        this.clientService = clientService;
        this.weekService = weekService;
        this.workoutService = workoutService;
    }

    @GetMapping("/all")
    public String loadAllPrograms(Model model, Principal principal) {

        UserDTO loggedUser = userService.getUserByUsername(principal.getName());
        if (loggedUser.getTitle().equals(UserTitleEnum.CLIENT)) {
            ClientDTO clientDTO = clientService.getClientByUsername(loggedUser.getUsername());
            List<ProgramDTO> allPrograms = programService.loadAllProgramsByCoachId(clientDTO.getCoach().getId());
            model.addAttribute("allPrograms", allPrograms);
        }

        return "programs";
    }

    @GetMapping("/{programId}")
    public String loadProgramById(@PathVariable("programId") Long programId, Model model, Principal principal) {
        ProgramDTO programDTO = programService.getById(programId);

        ClientDTO clientDTO = clientService.getClientByUsername(principal.getName());

        List<WeekEntity> allWeeksByProgramId = programService.getAllWeeksByProgramId(programId);

        model.addAttribute("program", programDTO);
        model.addAttribute("allWeeks", allWeeksByProgramId);
        model.addAttribute("allWorkouts", allWeeksByProgramId);
        model.addAttribute("user", clientDTO);


        return "program-details";
    }

    @GetMapping("/create")
    public String loadProgramCreation(@ModelAttribute("programCreationDTO") ProgramCreationDTO programCreationDTO, Model model) {

        List<WorkoutDTO> allWorkouts = workoutService.getAllWorkouts();

        model.addAttribute("allWorkouts", allWorkouts);

        return "create-program";
    }

    @PostMapping("/create")
    public String createProgram(@ModelAttribute("programCreationDTO") ProgramCreationDTO programCreationDTO, Principal principal) {
        ProgramEntity program = programService.createProgram(programCreationDTO, principal.getName());

        return "redirect:/programs/create/" + program.getId();
    }

    @GetMapping("/create/{programId}")
    public String loadProgramWorkoutCreation(@PathVariable("programId") Long programId, Model model) {

        List<WorkoutDTO> allWorkouts = workoutService.getAllWorkouts();
        ProgramDTO program = programService.getProgramById(programId);
        List<WeekCreationDTO> weeks = program.getWeeks();
        List<DayCreationDTO> allDays = weekService.getAllDays();

        model.addAttribute("allWorkouts", allWorkouts);
        model.addAttribute("program", program);
        model.addAttribute("weeks", weeks);
        model.addAttribute("weekCreationDTO", new WeekCreationDTO());

        return "program-add-workouts";
    }

    @PostMapping("/create/{programId}")
    public String loadProgramWorkoutCreation(@PathVariable("programId") Long programId, @ModelAttribute("weekCreationDTO") WeekCreationDTO weekCreationDTO) {

        return "program-add-workouts";
    }
}
