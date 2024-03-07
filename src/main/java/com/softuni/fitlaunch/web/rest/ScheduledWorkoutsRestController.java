package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.view.ScheduledWorkoutView;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.CoachService;
import com.softuni.fitlaunch.service.ScheduleWorkoutService;
import com.softuni.fitlaunch.service.UserService;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class ScheduledWorkoutsRestController {

    private final ScheduleWorkoutService scheduleWorkoutService;

    private final UserService userService;

    private final CoachService coachService;

    private final ClientService clientService;

    public ScheduledWorkoutsRestController(ScheduleWorkoutService scheduleWorkoutService, UserService userService, CoachService coachService, ClientService clientService) {
        this.scheduleWorkoutService = scheduleWorkoutService;
        this.userService = userService;
        this.coachService = coachService;
        this.clientService = clientService;
    }

    @GetMapping("/{username}/calendar/scheduledWorkouts")
    public ResponseEntity<List<ScheduledWorkoutView>> getAllScheduledWorkouts(@PathVariable("username") String username) {
        UserDTO userByUsername = userService.getUserByUsername(username);
        if(userByUsername.getTitle().equals(UserTitleEnum.CLIENT)) {
            ClientDTO clientByUsername = clientService.getClientByUsername(username);
            List<ScheduledWorkoutView> allClientScheduledWorkouts = scheduleWorkoutService.getAllClientScheduledWorkouts(clientByUsername);
            return ResponseEntity.ok(allClientScheduledWorkouts);
        } else {
            CoachDTO coachByUsername = coachService.getCoachByUsername(username);
            scheduleWorkoutService.getAllCoachScheduledWorkouts(coachByUsername);
            List<ScheduledWorkoutView> allCoachScheduledWorkouts = scheduleWorkoutService.getAllCoachScheduledWorkouts(coachByUsername);
            return ResponseEntity.ok(allCoachScheduledWorkouts);
        }
    }


    @DeleteMapping("/{username}/calendar/scheduledWorkouts/{eventId}")
    public ResponseEntity<List<ScheduledWorkoutView>> deleteScheduledWorkout(@PathVariable("username") String username, @PathVariable("eventId") Long eventId) throws ObjectNotFoundException {
        scheduleWorkoutService.deleteScheduledWorkout(username, eventId);
        return ResponseEntity.ok().build();
    }
}
