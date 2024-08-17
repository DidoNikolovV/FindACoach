package com.softuni.fitlaunch.integration;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.entity.WeekMetricsEntity;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.CoachService;
import com.softuni.fitlaunch.service.UserService;
import com.softuni.fitlaunch.service.WeekMetricsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {

    @MockBean
    private ClientService clientService;
    @MockBean
    private CoachService coachService;
    @MockBean
    private UserService userService;
    @MockBean
    private WeekMetricsService weekMetricsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoadAllClientsForCoachGet() throws Exception {
        ClientDTO client = new ClientDTO();
        client.setUsername("testClient");

        CoachDTO coach = new CoachDTO();
        coach.setUsername("testCoach");
        coach.setClients(new ArrayList<>());
        coach.getClients().add(client);

        List<ClientDTO> clients = coach.getClients();

        when(coachService.getCoachByUsername("testCoach")).thenReturn(coach);
        when(clientService.loadAllByCoach(coach)).thenReturn(clients);

        mockMvc.perform(get("/clients/{coach}/all", "testCoach"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("clients", clients))
                .andExpect(view().name("clients"));
    }

    @Test
    public void testLoadClientDetailsGet() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("testClient");
        user.setCompletedWorkoutsIds(new ArrayList<>());

        ClientDTO client = new ClientDTO();
        client.setUsername("testClient");
        client.setScheduledWorkouts(new ArrayList<>());
        client.setDailyMetrics(new ArrayList<>());

        CoachDTO coach = new CoachDTO();
        coach.setUsername("testCoach");
        coach.setClients(new ArrayList<>());
        coach.getClients().add(client);
        client.setCoach(coach);

        List<WeekMetricsEntity> allWeeks = new ArrayList<>();

        when(userService.getUserByUsername("testClient")).thenReturn(user);
        when(clientService.getClientByUsername("testClient")).thenReturn(client);
        when(weekMetricsService.getAll()).thenReturn(allWeeks);
        when(weekMetricsService.calculateAverageByClient(client)).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/clients/{clientName}/details", "testClient"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("client", client))
                .andExpect(model().attribute("coach", coach))
                .andExpect(model().attribute("scheduledWorkouts", client.getScheduledWorkouts()))
                .andExpect(model().attribute("allWeeks", allWeeks))
                .andExpect(view().name("client-details"));
    }

    @Test
    @WithMockUser(username = "testClient", roles = {"CLIENT"})
    void testLoadClientWeightInputGet() throws Exception {
        ClientDTO client = new ClientDTO();
        client.setUsername("testClient");
        client.setScheduledWorkouts(new ArrayList<>());

        when(clientService.getClientByUsername("testClient")).thenReturn(client);

        mockMvc.perform(get("/clients/daily-metrics/{clientName}", "testClient"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("client", client))
                .andExpect(model().attribute("dailyMetricsDTO", new DailyMetricsDTO()))
                .andExpect(model().attribute("activePage", "dailyMetrics"))
                .andExpect(view().name("daily-metrics"));
    }

    @Test
    void testSubmitClientWeightInputPost() throws Exception {
        DailyMetricsDTO dailyWeightDTO = new DailyMetricsDTO();
        ClientDTO client = new ClientDTO();
        client.setUsername("testClient");

        mockMvc.perform(post("/clients/daily-metrics/{clientName}", "testClient")
                        .flashAttr("dailyMetricsDTO", dailyWeightDTO)
                        .with(csrf())) // Add CSRF token manually
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }
}
