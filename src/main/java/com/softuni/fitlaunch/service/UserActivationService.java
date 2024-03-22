package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.UserActivationCodeEntity;
import com.softuni.fitlaunch.model.events.UserRegisteredEvent;
import com.softuni.fitlaunch.repository.UserActivationCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserActivationService {

    @Value("${app.activation-code.symbols}")
    private String ACTIVATION_CODE_SYMBOLS;

    @Value("${app.activation-code.length}")
    private int ACTIVATION_CODE_LENGTH;

    private final EmailService emailService;

    private final UserActivationCodeRepository userActivationCodeRepository;

    private final UserService userService;

    public UserActivationService(EmailService emailService, UserActivationCodeRepository userActivationCodeRepository, UserService userService) {
        this.emailService = emailService;
        this.userActivationCodeRepository = userActivationCodeRepository;
        this.userService = userService;
    }

    @EventListener(UserRegisteredEvent.class)
    public void userRegistered(UserRegisteredEvent userRegisteredEvent) {
        String activationCode = createActivationCode(userRegisteredEvent.getUserEmail());
        emailService.sendRegistrationEmail(userRegisteredEvent.getUserEmail(), userRegisteredEvent.getUsernames(), activationCode);
    }

    public void cleanUpObsoleteActivationLinks() {
        userActivationCodeRepository.deleteAll();
    }

    public String createActivationCode(String email) {
        String activationCode = generateActivationCode();

        UserActivationCodeEntity userActivationCodeEntity = new UserActivationCodeEntity();
        userActivationCodeEntity.setActivationCode(activationCode);
        userActivationCodeEntity.setCreated(Instant.now());
        userActivationCodeEntity.setUser(userService.getUserEntityByEmail(email));

        userActivationCodeRepository.save(userActivationCodeEntity);

        return activationCode;
    }

    private String generateActivationCode() {
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
