package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.view.UserCoachDetailsView;
import com.softuni.fitlaunch.model.dto.view.UserCoachView;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.repository.CoachRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CoachServiceTest {
    @Mock
    private CoachRepository coachRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ClientService clientService;
    @InjectMocks
    private CoachService underTest;

    public CoachServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCoaches_whenClientsWantsToPickACoach_thenReturnPageWithThem() {
        Page mock = Mockito.mock(Page.class);

        CoachEntity coach = new CoachEntity();
        coach.setId(1L);
        coach.setUsername("test");

        UserCoachView coachView = new UserCoachView();
        coachView.setId(1L);
        coachView.setUsername("test");

        when(coachRepository.findAll((Pageable) any())).thenReturn(mock);
        when(modelMapper.map(coach, UserCoachView.class)).thenReturn(coachView);

        underTest.getAllCoaches(any());

        verify(coachRepository, times(1)).findAll((Pageable) any());
    }

    @Test
    void testGetCoachById_whenCoachExists_thenReturnIt() {
        CoachEntity coach = new CoachEntity();
        coach.setCertificates(new ArrayList<>());
        coach.setClients(new ArrayList<>());
        coach.setScheduledWorkouts(new ArrayList<>());
        coach.setId(1L);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));

        underTest.getCoachById(1L);

        verify(coachRepository, times(1)).findById(1L);
    }

    @Test
    void testAddClient_whenClientsChooseCoach_thenAddItAndSave() {
        CoachEntity coach = new CoachEntity();
        coach.setCertificates(new ArrayList<>());
        coach.setClients(new ArrayList<>());
        coach.setScheduledWorkouts(new ArrayList<>());
        coach.setId(1L);

        ClientEntity client = new ClientEntity();
        client.setUsername("test");
        client.setCoach(coach);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setUsername("test");
        clientDTO.setWeightGoal(75.0);
        clientDTO.setPerformanceGoals("Test goals");
        clientDTO.setBodyCompositionGoal("Lose fat");

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(clientService.getClientEntityByUsername("test")).thenReturn(client);

        underTest.addClient(1L, clientDTO);

        verify(coachRepository, times(1)).save(coach);
    }

    @Test
    void testGetCoachDetailsById_whenCoachExists_thenReturnDetailsForHim() {
        CoachEntity coach = new CoachEntity();
        coach.setCertificates(new ArrayList<>());
        coach.setClients(new ArrayList<>());
        coach.setScheduledWorkouts(new ArrayList<>());
        coach.setId(1L);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));

        UserCoachDetailsView coachDetailsById = underTest.getCoachDetailsById(1L);

        verify(coachRepository, times(1)).findById(1L);
        assertThat(coachDetailsById).isNotNull();
    }

    @Test
    void testGetCoachByUsername_whenCoachWithThisUsernameExists_thenReturnCoachDto() {
        CoachEntity coach = new CoachEntity();
        coach.setCertificates(new ArrayList<>());
        coach.setClients(new ArrayList<>());
        coach.setScheduledWorkouts(new ArrayList<>());
        coach.setId(1L);
        coach.setUsername("test");

        CoachDTO coachDto = new CoachDTO();
        coachDto.setCertificates(new ArrayList<>());
        coachDto.setId(1L);
        coachDto.setScheduledWorkouts(new ArrayList<>());
        coachDto.setClients(new ArrayList<>());
        coachDto.setUsername(coach.getUsername());


        when(coachRepository.findByUsername("test")).thenReturn(Optional.of(coach));
        when(modelMapper.map(coach, CoachDTO.class)).thenReturn(coachDto);

        CoachDTO result = underTest.getCoachByUsername("test");

        verify(coachRepository, times(1)).findByUsername("test");
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(coachDto.getUsername());
    }

    @Test
    void testGetCoachClientById_whenCoachHasThisClient_thenReturnClientDto() {
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setUsername("test client");

        CoachEntity coach = new CoachEntity();
        coach.setCertificates(new ArrayList<>());
        coach.setClients(List.of(client));
        coach.setScheduledWorkouts(new ArrayList<>());
        coach.setId(1L);
        coach.setUsername("test");

        CoachDTO coachDto = new CoachDTO();

        ClientDTO clientDto = new ClientDTO();
        clientDto.setUsername(clientDto.getUsername());
        clientDto.setId(1L);
        clientDto.setCoach(coachDto);

        coachDto.setUsername(coachDto.getUsername());
        coachDto.setId(1L);
        coachDto.setUsername("test");
        coachDto.setClients(List.of(clientDto));

        when(coachRepository.findByUsername("test")).thenReturn(Optional.of(coach));
        when(clientService.getClientEntityByUsername("test client")).thenReturn(client);
        when(modelMapper.map(client, ClientDTO.class)).thenReturn(clientDto);

        ClientDTO coachClientById = underTest.getCoachClientById(coachDto, 1L);

        assertThat(coachClientById).isNotNull();
        verify(coachRepository, times(1)).findByUsername("test");
        verify(clientService, times(1)).getClientEntityByUsername("test client");
    }

    @Test
    void testRegisterCoach_whenCoachDoesNotExists_thenSaveItInTheDatabase(){
        when(modelMapper.map(any(), any())).thenReturn(any());

        underTest.registerCoach(any());

        verify(coachRepository, times(1)).save(any());
    }

    @Test
    void testGetCoachEntityByUsername_whenCoachWithThisUsernameExists_thenReturnIt() {
        CoachEntity coach = new CoachEntity();
        coach.setCertificates(new ArrayList<>());
        coach.setClients(new ArrayList<>());
        coach.setScheduledWorkouts(new ArrayList<>());
        coach.setId(1L);
        coach.setUsername("test");

        when(coachRepository.findByUsername("test")).thenReturn(Optional.of(coach));

        assertDoesNotThrow(() -> underTest.getCoachEntityByUsername("test"));

        verify(coachRepository, times(1)).findByUsername("test");
    }

    @Test
    void testGetCoachEntityByUsername_whenCoachWithThisUsernameDoesNotExist_thenThrowException() {

        assertThrows(ResourceNotFoundException.class,
                () -> underTest.getCoachEntityByUsername("test"));
    }

    @Test
    void testGetCoachEntityById_whenCoachWithThisIdExists_thenReturnHim() {
        CoachEntity coach = new CoachEntity();
        coach.setId(1L);
        coach.setUsername("test");
        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));

        assertDoesNotThrow(() -> underTest.getCoachEntityById(1L));

        verify(coachRepository, times(1)).findById(1L);

    }

    @Test
    void testGetCoachEntityById_whenCoachWithThisIdDoesNotExist_thenThrowException() {
        assertThrows(ResourceNotFoundException.class,
                () -> underTest.getCoachEntityById(1L));
    }

}