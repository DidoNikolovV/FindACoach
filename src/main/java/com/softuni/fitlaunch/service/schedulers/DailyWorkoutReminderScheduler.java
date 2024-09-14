package com.softuni.fitlaunch.service.schedulers;


import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.service.EmailService;
import com.softuni.fitlaunch.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyWorkoutReminderScheduler {


    private final EmailService emailService;

    private final UserService userService;

    public DailyWorkoutReminderScheduler(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void sendWorkoutReminders() {
        List<UserDTO> allUsers = userService.getAllUsers();

        emailService.sendReminderEmail(allUsers);
    }

}
