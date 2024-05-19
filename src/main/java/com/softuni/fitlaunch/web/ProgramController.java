package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.program.ProgramCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.week.WeekCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.enums.DaysEnum;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.ProgramService;
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
import java.util.Arrays;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/programs")
public class ProgramController {

    private final ProgramService programService;


    private final ClientService clientService;


    private final WorkoutService workoutService;


    public ProgramController(ProgramService programService, ClientService clientService, WorkoutService workoutService) {
        this.programService = programService;
        this.clientService = clientService;
        this.workoutService = workoutService;
    }

    @GetMapping("/all")
    public String loadAllPrograms(Model model, Principal principal) {
        List<ProgramDTO> allPrograms = programService.loadAllPrograms(principal.getName());

        model.addAttribute("allPrograms", allPrograms);

        return "programs";
    }

    @GetMapping("/{programId}")
    public String loadProgramById(@PathVariable("programId") Long programId, Model model, Principal principal) {
        ProgramDTO programDTO = programService.getById(programId);

        ClientDTO clientDTO = clientService.getClientByUsername(principal.getName());

        List<ProgramWeekEntity> allWeeksByProgramId = programService.getAllWeeksByProgramId(programId);

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
//        List<ProgramWeekDTO> weeks = program.getWeeks();
        List<DaysEnum> allDays = Arrays.stream(DaysEnum.values()).toList();

        model.addAttribute("allWorkouts", allWorkouts);
        model.addAttribute("program", program);
//        model.addAttribute("weeks", weeks);
        model.addAttribute("weekCreationDTO", new WeekCreationDTO());
        model.addAttribute("days", allDays);

        return "program-add-workouts";
    }

    @PostMapping("/create/{programId}")
    public String loadProgramWorkoutCreation(@PathVariable("programId") Long programId,
                                             @ModelAttribute("weekCreationDTO") WeekCreationDTO weekCreationDTO) {

        programService.addWeekWithWorkouts(weekCreationDTO, programId);
        return "redirect:/programs/create/" + programId;
    }

    @GetMapping("/details/{programId}")
    public String loadProgram(@PathVariable("programId") Long programId, Model model) {
        ProgramDTO program = programService.getProgramById(programId);
        model.addAttribute("program", program);

        return "program-details2";
    }

    @GetMapping("/{programId}/weeks/{weekId}")
    public String loadWeek(@PathVariable("programId") Long programId, @PathVariable("weekId") Long weekId, Model model, Principal principal) {
        ProgramDTO program = programService.getProgramById(programId);
        ProgramWeekDTO week = programService.getWeekById(weekId, programId, principal.getName());

        model.addAttribute("program", program);
        model.addAttribute("week", week);

        return "week-details";
    }
}
