package com.softuni.fitlaunch.web;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.model.entity.ProgressPicture;
import com.softuni.fitlaunch.model.entity.WeekMetricsEntity;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.CoachService;
import com.softuni.fitlaunch.service.UserService;
import com.softuni.fitlaunch.service.WeekMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    private final CoachService coachService;

    private final UserService userService;

    private final WeekMetricsService weekMetricsService;


    public ClientController(ClientService clientService, CoachService coachService, UserService userService, WeekMetricsService weekMetricsService) {
        this.clientService = clientService;
        this.coachService = coachService;
        this.userService = userService;
        this.weekMetricsService = weekMetricsService;
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
        CoachDTO coach = client.getCoach();

        client.setCompletedWorkouts(user.getCompletedWorkoutsIds());
        List<ScheduledWorkoutDTO> scheduledWorkouts = client.getScheduledWorkouts();
        List<DailyMetricsDTO> metrics = client.getDailyMetrics();

        double weightProgress = clientService.calculcateWeightProgress(client.getWeight(), client.getWeightGoal());

        List<WeekMetricsEntity> allWeeks = weekMetricsService.getAll();

        Map<Integer, DailyMetricsDTO> average = weekMetricsService.calculateAverageByClient(client);


        model.addAttribute("client", client);
        model.addAttribute("coach", coach);
        model.addAttribute("scheduledWorkouts", scheduledWorkouts);
        model.addAttribute("metrics", metrics);
        model.addAttribute("allWeeks", allWeeks);
        model.addAttribute("average", average);
        model.addAttribute("weightProgress", weightProgress);

        return "client-details";
    }

    @GetMapping("/daily-metrics/{clientName}")
    public String loadClientWeightInput(@PathVariable("clientName") String clientName, Model model) {
        ClientDTO client = clientService.getClientByUsername(clientName);

        model.addAttribute("client", client);
        model.addAttribute("dailyMetricsDTO", new DailyMetricsDTO());
        model.addAttribute("activePage", "dailyMetrics");

        return "daily-metrics";
    }

    @PostMapping("/daily-metrics/{clientName}")
    public String submitClientWeightInput(@PathVariable("clientName") String clientName, @ModelAttribute("dailWeightDTO") DailyMetricsDTO dailyWeightDTO) {
        clientService.saveDailyMetrics(clientName, dailyWeightDTO);

        return "redirect:/";
    }

    @GetMapping("/{clientUsername}/progress")
    public String showProgressPage(@PathVariable("clientUsername") String clientUsername, @PageableDefault(
            size = 3,
            sort = "id"
    ) Pageable pageable, Model model) {
        ClientDTO client = clientService.getClientByUsername(clientUsername);
        Page<ProgressPicture> pictures = clientService.getProgressPicturesByClientUsername(clientUsername, pageable);

        model.addAttribute("client", client);
        model.addAttribute("progressPictures", pictures);

        return "client_progress_pictures";
    }

    @PostMapping("/{clientUsername}/progress")
    public String handleFileUpload(@PathVariable("clientUsername") String clientUsername, @RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "redirect:/progress";
        }
        try {
            clientService.addProgressPicture(clientUsername, file);
            model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'.");

        } catch (Exception e) {
            log.error("Failed to upload {}.", file.getOriginalFilename());
            model.addAttribute("message", "Failed to upload '" + file.getOriginalFilename() + "'.");
        }

        return String.format("redirect:/clients/%s/progress", clientUsername);
    }
}
