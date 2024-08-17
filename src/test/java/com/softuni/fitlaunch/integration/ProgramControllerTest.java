package com.softuni.fitlaunch.integration;

import com.softuni.fitlaunch.model.dto.program.ProgramCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.week.WeekCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.enums.DaysEnum;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.ProgramService;
import com.softuni.fitlaunch.service.UserProgressService;
import com.softuni.fitlaunch.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgramService programService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private UserProgressService userProgressService;

    @Test
    @WithMockUser(username = "testUser")
    public void testLoadAllPrograms() throws Exception {
        ProgramDTO program = new ProgramDTO();
        program.setId(1L);
        program.setName("Beginner");
        program.setWeeks(new ArrayList<>());
        List<ProgramDTO> allPrograms = new ArrayList<>();
        allPrograms.add(program);

        when(programService.loadAllPrograms("testUser")).thenReturn(allPrograms);

        mockMvc.perform(get("/programs/all"))
                .andExpect(model().attribute("allPrograms", allPrograms))
                .andExpect(model().attribute("activePage", "allPrograms"))
                .andExpect(view().name("programs"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testLoadProgramById() throws Exception {
        Long programId = 1L;

        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setId(1L);
        programEntity.setName("Beginner");
        programEntity.setWeeks(new ArrayList<>());

        ProgramDTO programDTO = new ProgramDTO();
        programDTO.setId(1L);
        programDTO.setName("Beginner");
        programDTO.setWeeks(new ArrayList<>());
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setUsername("testUser");

        List<ProgramWeekEntity> allWeeksByProgramId = new ArrayList<>();
        ProgramWeekEntity programWeekEntity = new ProgramWeekEntity();
        programWeekEntity.setId(1L);
        programWeekEntity.setNumber(1);
        programWeekEntity.setProgram(programEntity);
        allWeeksByProgramId.add(programWeekEntity);

        Map<Long, Boolean> completedWorkouts = new HashMap<>();


        when(programService.getById(programId)).thenReturn(programDTO);
        when(clientService.getClientByUsername("testUser")).thenReturn(clientDTO);
        when(programService.getAllWeeksByProgramId(programId)).thenReturn(allWeeksByProgramId);
        when(userProgressService.getCompletedWorkouts(clientDTO.getUsername(), programId)).thenReturn(completedWorkouts);

        mockMvc.perform(get("/programs/{programId}", programId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("program", programDTO))
                .andExpect(model().attribute("allWeeks", allWeeksByProgramId))
                .andExpect(model().attribute("completedWorkouts", completedWorkouts))
                .andExpect(model().attribute("user", clientDTO))
                .andExpect(view().name("program-details"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testLoadProgramCreation() throws Exception {
        WorkoutDTO workout = new WorkoutDTO();
        workout.setId(1L);
        workout.setName("Full Body");

        List<WorkoutDTO> allWorkouts = new ArrayList<>();
        allWorkouts.add(workout);

        when(workoutService.getAllWorkouts()).thenReturn(allWorkouts);

        mockMvc.perform(get("/programs/create"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allWorkouts", allWorkouts))
                .andExpect(view().name("create-program"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testLoadProgramWorkoutCreation() throws Exception {
        WorkoutDTO workout = new WorkoutDTO();
        workout.setId(1L);
        workout.setName("Full Body");

        List<WorkoutDTO> allWorkouts = new ArrayList<>();
        allWorkouts.add(workout);

        ProgramDTO program = new ProgramDTO();
        program.setId(1L);
        program.setName("Beginner");
        program.setWeeks(new ArrayList<>());

        when(workoutService.getAllWorkouts()).thenReturn(allWorkouts);
        when(programService.getProgramById(1L, "testUser")).thenReturn(program);
        List<DaysEnum> allDays = Arrays.stream(DaysEnum.values()).toList();

        mockMvc.perform(get("/programs/create/{programId}", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allWorkouts", allWorkouts))
                .andExpect(model().attribute("program", program))
                .andExpect(model().attribute("weekCreationDTO", new WeekCreationDTO()))
                .andExpect(model().attribute("days", allDays))
                .andExpect(view().name("program-add-workouts"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = {"COACH"})
    public void testCreateProgram() throws Exception {
        ProgramCreationDTO programCreationDTO = new ProgramCreationDTO();
        programCreationDTO.setName("New Program");

        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setId(1L);
        when(programService.createProgram(any(ProgramCreationDTO.class), anyString()))
                .thenReturn(programEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/programs/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "New Program")
                        .principal(() -> "testUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/programs/create/1"));
    }

    @Test
    @WithMockUser("testUser")
    public void testLoadProgramWorkoutCreationGet() throws Exception {
        Long programId = 1L;
        List<WorkoutDTO> allWorkouts = Arrays.asList(new WorkoutDTO(), new WorkoutDTO());
        ProgramDTO programDTO = new ProgramDTO();
        programDTO.setId(programId);

        when(workoutService.getAllWorkouts()).thenReturn(allWorkouts);
        when(programService.getProgramById(anyLong(), anyString())).thenReturn(programDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/programs/create/{programId}", programId)
                        .param("programId", programId.toString())
                        .principal(() -> "testUser"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allWorkouts", "program", "weekCreationDTO", "days"))
                .andExpect(view().name("program-add-workouts"));
    }

    @Test
    @WithMockUser("testUser")
    public void testRedirectToProgramDetailsAfterWorkoutCreation() throws Exception {
        Long programId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.post("/programs/create/{programId}", programId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/programs/details/" + programId));
    }


    @Test
    @WithMockUser("testUser")
    public void testLoadWeekDetails() throws Exception {
        Long programId = 1L;
        Long weekId = 1L;
        ProgramDTO programDTO = new ProgramDTO();
        ProgramWeekDTO weekDTO = new ProgramWeekDTO();

        when(programService.getProgramById(anyLong(), anyString())).thenReturn(programDTO);
        when(programService.getWeekById(anyLong(), anyLong(), anyString())).thenReturn(weekDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/programs/{programId}/weeks/{weekId}", programId, weekId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("program", "week"))
                .andExpect(view().name("week-details"));
    }


}
