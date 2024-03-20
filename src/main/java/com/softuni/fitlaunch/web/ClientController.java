package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.CoachService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ClientController {

    private final ClientService clientService;

    private final CoachService coachService;

    public ClientController(ClientService clientService, CoachService coachService) {
        this.clientService = clientService;
        this.coachService = coachService;
    }


    @GetMapping("/clients/{coach}/all")
    public String loadAllClientsForCoach(@PathVariable("coach") String username, Model model) {
        CoachDTO coach = coachService.getCoachByUsername(username);
        List<ClientDTO> clients = clientService.loadAllByCoach(coach);

        model.addAttribute("clients", clients);

        return "clients";
    }
}
