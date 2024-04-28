package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.CoachService;
import com.softuni.fitlaunch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    private final CoachService coachService;

    private final UserService userService;

    public ClientController(ClientService clientService, CoachService coachService, UserService userService) {
        this.clientService = clientService;
        this.coachService = coachService;
        this.userService = userService;
    }


    @GetMapping("/{coach}/all")
    public String loadAllClientsForCoach(@PathVariable("coach") String username, Model model) {
        CoachDTO coach = coachService.getCoachByUsername(username);
        List<ClientDTO> clients = clientService.loadAllByCoach(coach);

        model.addAttribute("clients", clients);

        return "clients";
    }

    @GetMapping("/{clientName}/details")
    public String loadClientDetails(@PathVariable("clientName") String clientName, Model model) {
        UserDTO user = userService.getUserByUsername(clientName);
        ClientDTO client = clientService.getClientByUsername(clientName);
        client.setCompletedWorkouts(user.getCompletedWorkoutsIds());

        model.addAttribute("client", client);

        return "client-details";

    }
}
