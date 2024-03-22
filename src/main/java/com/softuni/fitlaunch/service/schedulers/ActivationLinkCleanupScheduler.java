package com.softuni.fitlaunch.service.schedulers;


import com.softuni.fitlaunch.service.UserActivationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActivationLinkCleanupScheduler {

    private final UserActivationService userActivationService;

    public ActivationLinkCleanupScheduler(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @Scheduled(fixedRate = 10_000, initialDelay = 10_000)
    public void cleanUp() {
        userActivationService.cleanUpObsoleteActivationLinks();
    }

}
