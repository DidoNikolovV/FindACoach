package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.entity.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmailServiceTest {

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmailService emailService;

    public EmailServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Disabled
    void sendRegistrationEmail_whenEmailIsValid_thenEmailIsSent() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(templateEngine.process(eq("email/registration-email"), any(Context.class)))
                .thenReturn("email-body");

        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);

        emailService.sendRegistrationEmail("user@example.com", "username", "activationCode");

        verify(javaMailSender, times(1)).send(mimeMessageCaptor.capture());

        MimeMessage capturedMessage = mimeMessageCaptor.getValue();
    }

    @Test
    @Disabled
    void sendReminderEmail_whenUsersListIsProvided_thenEmailsAreSent() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("user@example.com");

        when(modelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);

        emailService.sendReminderEmail(List.of(userDTO));

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendReminderEmail_whenMessagingExceptionThrown_thenRuntimeExceptionIsThrown() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("user@example.com");

        when(modelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);
        doThrow(MessagingException.class).when(javaMailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () ->
                emailService.sendReminderEmail(List.of(userDTO)));
    }
}
