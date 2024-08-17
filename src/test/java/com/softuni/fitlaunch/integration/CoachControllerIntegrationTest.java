package com.softuni.fitlaunch.integration;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.view.UserCoachDetailsView;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.CoachService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CoachControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoachService coachService;

    @MockBean
    private ClientService clientService;


    @Test
    @WithMockUser(username = "testCoach", roles = {"COACH"})
    public void testCoachDetailsGet() throws Exception {
        UserCoachDetailsView coachDetailsView = new UserCoachDetailsView();

        when(coachService.getCoachDetailsById(1L)).thenReturn(coachDetailsView);

        mockMvc.perform(get("/coaches/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("coachDetails", coachDetailsView))
                .andExpect(MockMvcResultMatchers.view().name("coach-details"));
    }

    @Test
    @WithMockUser(username = "testClient", roles = {"CLIENT"})
    public void testCoachDetailsInformationGet() throws Exception {

        ClientDTO client = new ClientDTO();
        client.setUsername("testClient");

        CoachDTO coach = new CoachDTO();
        coach.setUsername("testCoach");

        when(clientService.getClientByUsername("testClient")).thenReturn(client);
        when(coachService.getCoachById(1L)).thenReturn(coach);

        mockMvc.perform(get("/coaches/{coachId}/client/information", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("coach", coach))
                .andExpect(MockMvcResultMatchers.model().attribute("client", client))
                .andExpect(MockMvcResultMatchers.view().name("client-information-form"));
    }

    @Test
    @WithMockUser(username = "testCoach", roles = {"COACH"})
    public void testClientDetailsPost() throws Exception {
        ClientDTO client = new ClientDTO();
        client.setUsername("testClient");
        mockMvc.perform(post("/coaches/{coachId}/client/information", 1L)
                        .flashAttr("clientDTO", client)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    @WithMockUser(username = "testCoach", roles = {"COACH"})
    public void testCoachClientDetailsGet() throws Exception {
        CoachDTO coach = new CoachDTO();
        coach.setUsername("testCoach");

        ClientDTO client = new ClientDTO();
        client.setId(1L);
        client.setUsername("testClient");
        client.setCoach(coach);

        when(coachService.getCoachByUsername("testCoach")).thenReturn(coach);
        when(coachService.getCoachClientById(coach, 1L)).thenReturn(client);

        mockMvc.perform(get("/coaches/clients/{clientId}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("client", client))
                .andExpect(MockMvcResultMatchers.view().name("client-details"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"CLIENT"})
    public void testMyCoach() throws Exception {
        CoachDTO coach = new CoachDTO();
        coach.setId(1L);
        coach.setUsername("testCoach");
        coach.setImgUrl("testUrl");
        coach.setScheduledWorkouts(new ArrayList<>());
        coach.setCertificates(new ArrayList<>());
        coach.setEmail("test@email.com");
        coach.setRating(5.0);
        coach.setDescription("test desc");

        when(coachService.getCoachById(1L)).thenReturn(coach);

        mockMvc.perform(get("/coaches/coach/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("coach", coach))
                .andExpect(MockMvcResultMatchers.model().attribute("activePage", "coaches"))
                .andExpect(MockMvcResultMatchers.view().name("coach"));
    }

}
