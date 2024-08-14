package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.ScheduledWorkoutEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.repository.ScheduledWorkoutRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduleWorkoutServiceTest {

    @Mock
    private ScheduledWorkoutRepository scheduledWorkoutRepository;
    @Mock
    private CoachService coachService;
    @Mock
    private ClientService clientService;
    @Mock
    private UserService userService;

    @InjectMocks
    private ScheduleWorkoutService underTest;

    public ScheduleWorkoutServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testScheduleWorkout_whenClientScheduleWorkout_thenScheduleWorkout() {
        ClientEntity client = new ClientEntity();
        client.setUsername("test client");
        client.setId(1L);

        CoachEntity coach = new CoachEntity();
        coach.setUsername("test coach");
        coach.setId(1L);

        when(clientService.getClientEntityByUsername("test client")).thenReturn(client);
        when(coachService.getCoachEntityByUsername("test coach")).thenReturn(coach);

        underTest.scheduleWorkout("test client", "test coach", String.valueOf(LocalDate.now()));

        verify(clientService, times(1)).getClientEntityByUsername("test client");
        verify(coachService, times(1)).getCoachEntityByUsername("test coach");
        verify(scheduledWorkoutRepository, times(1)).save(any());
    }

    @Test
    void testGetAllScheduledWorkouts_whenClientWantsToCheckThem_thenReturnListOfThem(){
        UserEntity user = new UserEntity();
        user.setTitle(UserTitleEnum.CLIENT);
        user.setId(1L);
        user.setUsername("test");

        ClientEntity client = new ClientEntity();
        client.setUsername("test");
        client.setId(1L);

        CoachEntity coach = new CoachEntity();
        coach.setUsername("test coach");
        coach.setId(1L);

        List<ScheduledWorkoutEntity> allScheduledWorkouts = new ArrayList<>();
        ScheduledWorkoutEntity scheduledWorkoutEntity = new ScheduledWorkoutEntity();
        scheduledWorkoutEntity.setId(1L);
        scheduledWorkoutEntity.setClient(client);
        scheduledWorkoutEntity.setCoach(coach);
        scheduledWorkoutEntity.setScheduledDate(LocalDate.now());
        allScheduledWorkouts.add(scheduledWorkoutEntity);

        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(clientService.getClientEntityByUsername("test")).thenReturn(client);
        when(scheduledWorkoutRepository.findAllByClientId(1L)).thenReturn(allScheduledWorkouts);

        underTest.getAllScheduledWorkouts("test");

        verify(userService, times(1)).getUserEntityByUsername("test");
        verify(clientService, times(1)).getClientEntityByUsername("test");
        verify(scheduledWorkoutRepository, times(1)).findAllByClientId(1L);
    }

    @Test
    void testGetAllScheduledWorkouts_whenCoachWantsToCheckThem_thenReturnListOfThem(){
        UserEntity user = new UserEntity();
        user.setTitle(UserTitleEnum.COACH);
        user.setId(1L);
        user.setUsername("test");

        ClientEntity client = new ClientEntity();
        client.setUsername("test client");
        client.setId(1L);

        CoachEntity coach = new CoachEntity();
        coach.setUsername("test");
        coach.setId(1L);

        List<ScheduledWorkoutEntity> allScheduledWorkouts = new ArrayList<>();
        ScheduledWorkoutEntity scheduledWorkoutEntity = new ScheduledWorkoutEntity();
        scheduledWorkoutEntity.setId(1L);
        scheduledWorkoutEntity.setClient(client);
        scheduledWorkoutEntity.setCoach(coach);
        scheduledWorkoutEntity.setScheduledDate(LocalDate.now());

        allScheduledWorkouts.add(scheduledWorkoutEntity);

        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(coachService.getCoachEntityByUsername("test")).thenReturn(coach);
        when(scheduledWorkoutRepository.findAllByClientId(1L)).thenReturn(allScheduledWorkouts);

        underTest.getAllScheduledWorkouts("test");

        verify(userService, times(1)).getUserEntityByUsername("test");
        verify(coachService, times(1)).getCoachEntityByUsername("test");
        verify(scheduledWorkoutRepository, times(1)).findAllByCoachId(1L);
    }

    @Test
    void testDeleteScheduledWorkout_whenWorkoutAlreadyScheduled_thenDeleteIt() {
        underTest.deleteScheduledWorkout(1L);
        verify(scheduledWorkoutRepository, times(1)).deleteById(1L);
    }

}