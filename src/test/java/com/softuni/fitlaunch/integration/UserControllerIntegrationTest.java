package com.softuni.fitlaunch.integration;


import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.view.ScheduledWorkoutView;
import com.softuni.fitlaunch.model.dto.view.UserProfileView;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.service.BlackListService;
import com.softuni.fitlaunch.service.ScheduleWorkoutService;
import com.softuni.fitlaunch.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {


    @MockBean
    private UserService userService;

    @MockBean
    private BlackListService blackListService;

    @MockBean
    private ScheduleWorkoutService scheduleWorkoutService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"CLIENT"})
    public void testUserProfileGet() throws Exception {
        UserProfileView userProfileView = new UserProfileView();
        userProfileView.setUsername("testuser");

        ScheduledWorkoutView scheduledWorkoutView = new ScheduledWorkoutView();

        when(userService.getUserProfileByUsername(anyString())).thenReturn(userProfileView);
        when(scheduleWorkoutService.getAllScheduledWorkouts(anyString())).thenReturn(Collections.singletonList(scheduledWorkoutView));

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("profilePictureDTO"))
                .andExpect(model().attribute("user", userProfileView))
                .andExpect(model().attributeExists("upcomingSessions"))
                .andExpect(model().attribute("upcomingSessions", Collections.singletonList(scheduledWorkoutView)));
    }


    @Test
    public void testRegisterGet() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("activePage"));
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"CLIENT"})
    public void testMyCalendar() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setTitle(UserTitleEnum.valueOf("CLIENT"));

        when(userService.getUserByUsername("testuser")).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/testuser/calendar"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("my-calendar"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userTitle"))
                .andExpect(MockMvcResultMatchers.model().attribute("userTitle", "CLIENT"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("activePage"))
                .andExpect(MockMvcResultMatchers.model().attribute("activePage", "myCalendar"));
    }

    @Test
    public void testContactUs() throws Exception {
        mockMvc.perform(get("/users/contact-us"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact-us"))
                .andExpect(MockMvcResultMatchers.model().attribute("activePage", "concatUs"));
    }

    @Test
    public void testLoginGet() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attribute("activePage", "login"));
    }

    @Test
    public void testActivateAccount_Success() throws Exception {
        String activationCode = "validActivationCode";

        // Mocking the service to return true when a valid activation code is provided
        when(userService.activateUser(activationCode)).thenReturn(true);

        mockMvc.perform(get("/users/activate/{activationCode}", activationCode))
                .andExpect(status().isOk())
                .andExpect(view().name("email/activation-success"));
    }

    @Test
    public void testActivateAccount_InvalidCode() throws Exception {
        String activationCode = "invalidActivationCode";

        // Mocking the service to return false when an invalid activation code is provided
        when(userService.activateUser(activationCode)).thenReturn(false);

        mockMvc.perform(get("/users/activate/{activationCode}", activationCode))
                .andExpect(status().isOk())
                .andExpect(view().name("email/activation-failed"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"CLIENT"})
    public void testLoginOnFailure() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        user.setActivated(true);

        when(userService.getUserByUsername("testUser")).thenReturn(user);

        mockMvc.perform(post("/users/login-error")
                        .flashAttr("username", "testUser")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("username", "testUser"))
                .andExpect(MockMvcResultMatchers.model().attribute("bad_credentials", "true"))
                .andExpect(view().name("login"));
    }

    @Test
    void register_whenValidUser_thenRedirectToLogin() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("password");
        userRegisterDTO.setEmail("test@example.com");
        userRegisterDTO.setTitle(UserTitleEnum.CLIENT.name());

        when(userService.register(any(UserRegisterDTO.class))).thenReturn(true);

        mockMvc.perform(post("/users/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/login"));

        verify(userService, times(1)).register(any(UserRegisterDTO.class));
    }

    @Test
    void register_whenBindingErrors_thenReturnRegisterView() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("differentPassword"); // to induce binding error
        userRegisterDTO.setEmail("test@example.com");

        mockMvc.perform(post("/users/register")
                        .flashAttr("userRegisterDTO", userRegisterDTO)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        verify(userService, times(0)).register(any(UserRegisterDTO.class));
    }



}
