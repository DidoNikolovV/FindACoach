package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.view.UserCoachDetailsView;
import com.softuni.fitlaunch.model.dto.view.UserCoachView;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.CoachService;
import jakarta.validation.Valid;
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


@Slf4j
@Controller
@RequestMapping("/coaches")
public class CoachController {

    private final CoachService coachService;

    private final ClientService clientService;


    public CoachController(CoachService coachService, ClientService clientService) {
        this.coachService = coachService;
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public String allCoaches(Principal principal, Model model, @PageableDefault(
            size = 6,
            sort = "id"
    ) Pageable pageable) {
        ClientDTO client = clientService.getClientByUsername(principal.getName());
        if (client.getCoach() != null) {
            return "redirect:/coaches/coach/" + client.getCoach().getId();
        }

        Page<UserCoachView> allCoaches = coachService.getAllCoaches(pageable);

        model.addAttribute("allCoaches", allCoaches);
        model.addAttribute("activePage", "coaches");

        return "coaches";
    }

    @GetMapping("/coach/{id}")
    public String myCoach(@PathVariable("id") Long id, Model model, ScheduledWorkoutDTO scheduledWorkoutDTO) {
        CoachDTO coach = coachService.getCoachById(id);


        model.addAttribute("scheduledWorkoutDTO", scheduledWorkoutDTO);
        model.addAttribute("coach", coach);
        model.addAttribute("activePage", "coaches");

        return "coach";
    }

    @GetMapping("/{id}")
    public String coachDetails(@PathVariable("id") Long id, Model model) {
        UserCoachDetailsView coachDetailsView = coachService.getCoachDetailsById(id);

        model.addAttribute("coachDetails", coachDetailsView);

        return "coach-details";
    }

    @GetMapping("/{coachId}/client/information")
    public String coachDetails(@PathVariable("coachId") Long coachId, Principal principal, Model model) {
        ClientDTO client = clientService.getClientByUsername(principal.getName());

        CoachDTO coachById = coachService.getCoachById(coachId);
        model.addAttribute("coach", coachById);
        model.addAttribute("client", client);

        return "client-information-form";
    }

    @PostMapping("/{coachId}/client/information")
    public String clientDetails(@PathVariable("coachId") Long coachId, @ModelAttribute("clientDTO") @Valid ClientDTO clientDTO, Principal principal) {
        coachService.addClient(coachId, clientDTO);
        return "redirect:/";
    }

    @GetMapping("/clients/{clientId}")
    public String coachClientDetails(@PathVariable("clientId") Long id, Principal principal, Model model) {
        CoachDTO coach = coachService.getCoachByUsername(principal.getName());
        ClientDTO client = coachService.getCoachClientById(coach, id);

        model.addAttribute("client", client);

        return "client-details";
    }
}
