package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.entity.UserActivationCodeEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.events.UserRegisteredEvent;
import com.softuni.fitlaunch.repository.UserActivationCodeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserActivationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private UserActivationCodeRepository userActivationCodeRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserActivationService underTest;

    private String userEmail = "test@example.com";
    private String username = "testUser";

    public UserActivationServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUserRegistered() {
        Object source = new Object();
        UserRegisteredEvent event = new UserRegisteredEvent(source, userEmail, username);

        when(userService.getUserEntityByEmail(userEmail)).thenReturn(new UserEntity());
        when(userActivationCodeRepository.save(any(UserActivationCodeEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        underTest.userRegistered(event);

        verify(emailService).sendRegistrationEmail(eq(userEmail), eq(username), any(String.class));
        verify(userActivationCodeRepository).save(any(UserActivationCodeEntity.class));
    }

    @Test
    void testCreateActivationCode_whenNewUserRegisters_thenCreateAndSaveIt() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        UserActivationCodeEntity userActivationCodeEntity = new UserActivationCodeEntity();
        userActivationCodeEntity.setCreated(Instant.now());

        when(userService.getUserEntityByEmail("test@example.com")).thenReturn(user);

        underTest.createActivationCode("test@example.com");

        verify(userActivationCodeRepository, times(1)).save(any());
    }

    @Test
    void testCleanUpObsoleteActivationLinks() {
        underTest.cleanUpObsoleteActivationLinks();

        verify(userActivationCodeRepository, times(1)).deleteAll();
    }

}