package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDetailsDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.view.UserCoachDetailsView;
import com.softuni.fitlaunch.model.dto.view.UserCoachView;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.service.CoachService;
import com.softuni.fitlaunch.service.ScheduleWorkoutService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/coaches")
public class CoachController {

    private final CoachService coachService;


    private final ScheduleWorkoutService scheduleWorkoutService;

    public CoachController(CoachService coachService, ScheduleWorkoutService scheduleWorkoutService) {
        this.coachService = coachService;
        this.scheduleWorkoutService = scheduleWorkoutService;
    }

    @GetMapping("/all")
    public String allCoaches(Principal principal, Model model) {
        ClientDTO clientByUsername = coachService.getClientByUsername(principal.getName());
        if(clientByUsername.getCoach() != null) {
            return "redirect:/coaches/coach/" + clientByUsername.getCoach().getId();
        }
        List<UserCoachView> allCoaches = coachService.getAllCoaches();

        model.addAttribute("allCoaches", allCoaches);

        return "coaches";
    }

    @GetMapping("/coach/{id}")
    public String myCoach(@PathVariable("id") Long id, Model model, ScheduledWorkoutDTO scheduledWorkoutDTO) {
        CoachDTO coach = coachService.getCoachById(id);

        model.addAttribute("scheduledWorkoutDTO", scheduledWorkoutDTO);
        model.addAttribute("coach", coach);

        return "coach";
    }

    @PostMapping("/coach/{coachId}/schedule")
    public String scheduleWorkout(@PathVariable("coachId") Long coachId, Model model, Principal principal, @Valid ScheduledWorkoutDTO scheduledWorkoutDTO) {
        CoachDTO coachById = coachService.getCoachById(coachId);
        ClientDTO clientByUsername = coachService.getClientByUsername(principal.getName());

        scheduleWorkoutService.scheduleWorkout(clientByUsername, coachById, scheduledWorkoutDTO.getScheduledDateTime());

        return "redirect:/index";
    }

    @GetMapping("/{id}")
    public String coachDetails(@PathVariable("id") Long id, Model model) {
        UserCoachDetailsView coachDetailsView = coachService.getCoachDetailsById(id);

        model.addAttribute("coachDetails", coachDetailsView);

        return "coach-details";
    }

    @GetMapping("/{coachId}/client/information")
    public String coachDetails(@PathVariable("coachId") Long coachId, Principal principal, Model model) {
        ClientDTO client = coachService.getClientByUsername(principal.getName());

        CoachDTO coachById = coachService.getCoachById(coachId);
        model.addAttribute("coach", coachById);
        model.addAttribute("client", client);
//        coachService.addClient(coachId, client);
        return "client-information-form";
    }

    @PostMapping("/{coachId}/client/information")
    public String clientDetails(@PathVariable("coachId") Long coachId,@ModelAttribute("clientDTO") @Valid ClientDetailsDTO clientDetailsDTO, Principal principal) {
        coachService.setClientDetails(principal.getName(), clientDetailsDTO);

        ClientDTO client = coachService.getClientByUsername(principal.getName());
        coachService.addClient(coachId, client);
        return "redirect:/";
    }

    @GetMapping("/clients/all")
    public String coachAllClients(Model model, Principal principal) {
        CoachDTO coach = coachService.getCoachByUsername(principal.getName());

        model.addAttribute("coach", coach);

        return "clients";
    }

    @GetMapping("/clients/{clientId}")
    public String coachClientDetails(@PathVariable("clientId") Long id, Principal principal, Model model) {
        CoachDTO coach = coachService.getCoachByUsername(principal.getName());
        ClientDTO coachClientById = coachService.getCoachClientById(coach, id);

        model.addAttribute("client", coachClientById);


        return "client-details";


    }
}
