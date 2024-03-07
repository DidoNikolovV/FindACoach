package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.UserActivationCodeEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.events.UserRegisteredEvent;
import com.softuni.fitlaunch.repository.UserActivationCodeRepository;
import com.softuni.fitlaunch.repository.UserRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserActivationService {

    private static final String ACTIVATION_CODE_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";

    private static final int ACTIVATION_CODE_LENGTH = 20;

    private final EmailService emailService;

    private final UserRepository userRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;

    public UserActivationService(EmailService emailService, UserRepository userRepository, UserActivationCodeRepository userActivationCodeRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
    }

    @EventListener(UserRegisteredEvent.class)
    public void userRegistered(UserRegisteredEvent userRegisteredEvent) {
        String activationCode = createActivationCode(userRegisteredEvent.getUserEmail());
        UserEntity user = userRepository.findByEmail(userRegisteredEvent.getUserEmail()).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        emailService.sendRegistrationEmail(userRegisteredEvent.getUserEmail(), userRegisteredEvent.getUsernames(), activationCode);
    }

    public void cleanUpObsoleteActivationLinks() {
        System.out.println("NOT YET");
    }

    public String createActivationCode(String userEmail) {

        String activationCode = generateActivationCode();

        UserActivationCodeEntity userActivationCodeEntity = new UserActivationCodeEntity();
        userActivationCodeEntity.setActivationCode(activationCode);
        userActivationCodeEntity.setCreated(Instant.now());
        userActivationCodeEntity.setUser(userRepository.findByEmail(userEmail).orElseThrow(() -> new ObjectNotFoundException("User not found")));

        userActivationCodeRepository.save(userActivationCodeEntity);


        return activationCode;
    }

    private static String generateActivationCode() {
        StringBuilder activationCode = new StringBuilder();
        Random random = new SecureRandom();

        for (int i = 0; i < ACTIVATION_CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(ACTIVATION_CODE_SYMBOLS.length());
            activationCode.append(ACTIVATION_CODE_SYMBOLS.charAt(randomIndex));
        }

        return activationCode.toString();
    }

    private LocalDateTime calculateActivationCodeExpiration() {
        // Implement a logic to calculate the expiration time for the activation code
        // For example, set it to expire after a certain period (e.g., 24 hours)
        return LocalDateTime.now().plusHours(24);
    }
}
