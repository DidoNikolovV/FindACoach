package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.WeekMetricsEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.repository.DailyMetricsRepository;
import com.softuni.fitlaunch.repository.ProgressPictureRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private DailyMetricsRepository dailyMetricsRepository;
    @Mock
    private WeekMetricsService weekMetricsService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private FileUpload fileUpload;
    @Mock
    private ProgressPictureRepository progressPictureRepository;
    @InjectMocks
    private ClientService underTest;

    public ClientServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testReigsterClient_whenClientDoesNotExist_thenSaveItInDatabase() {
        UserEntity user = new UserEntity();
        user.setUsername("test");

        ClientEntity client = new ClientEntity();
        client.setUsername(user.getUsername());

        when(modelMapper.map(user, ClientEntity.class)).thenReturn(client);

        underTest.registerClient(user);

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testGetClientByUsername_whenClientWithUsernameExists_thenReturnHim() {
        ClientEntity client = new ClientEntity();
        client.setUsername("test");

        ClientDTO clientDto = new ClientDTO();
        clientDto.setUsername(client.getUsername());

        when(clientRepository.findByUsername("test")).thenReturn(Optional.of(client));
        when(modelMapper.map(client, ClientDTO.class)).thenReturn(clientDto);

        underTest.getClientByUsername("test");

        verify(clientRepository, times(1)).findByUsername("test");
    }

    @Test
    void testGetClientByUsername_whenClientWithUsernameDoesNotExist_thenThrowException() {

        assertThrows(ResourceNotFoundException.class,
                () -> underTest.getClientEntityByUsername("test"));
    }

    @Test
    void testGetClientEntityByUsername_whenClientWithUsernameExists_thenReturnHim() {
        ClientEntity client = new ClientEntity();
        client.setUsername("test");

        when(clientRepository.findByUsername("test")).thenReturn(Optional.of(client));
        underTest.getClientEntityByUsername("test");

        verify(clientRepository, times(1)).findByUsername("test");
    }

    @Test
    void testGetClientEntityByUsername_whenClientWithUsernameDoesNotExist_thenThrowException() {
        assertThrows(ResourceNotFoundException.class,
                () -> underTest.getClientEntityByUsername("test"));
    }

    @Test
    void testLoadAllByCoach_whenSuchClientsExists_thenReturnListOfThem() {
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        when(clientRepository.findAllByCoachId(1L)).thenReturn(List.of(client));
    }

    @Test
    void testSaveDailyMetrics_whenClientReportNewDailyMetrics_thenSaveThemInDatabase() {
        ClientEntity client = new ClientEntity();
        client.setUsername("test");
        client.setId(1L);
        client.setDailyMetrics(new ArrayList<>());

        List<DailyMetricsEntity> metrics = new ArrayList<>();

        DailyMetricsDTO dailyMetricsDto = new DailyMetricsDTO();
        dailyMetricsDto.setMood(8);
        dailyMetricsDto.setCaloriesIntake(2200.0);
        dailyMetricsDto.setEnergyLevels(7);
        dailyMetricsDto.setStepsCount(10000.0);
        dailyMetricsDto.setWeight(74.0);

        DailyMetricsEntity dailyMetrics = new DailyMetricsEntity();
        dailyMetrics.setId(1L);
        dailyMetrics.setClient(client);
        dailyMetrics.setMood(8);
        dailyMetrics.setCaloriesIntake(2200.0);
        dailyMetrics.setEnergyLevels(7);
        dailyMetrics.setStepsCount(10000.0);
        dailyMetrics.setWeight(74.0);


        when(clientRepository.findByUsername("test")).thenReturn(Optional.of(client));
        when(dailyMetricsRepository.findAllByClientId(1L)).thenReturn(metrics);
        when(modelMapper.map(dailyMetricsDto, DailyMetricsEntity.class)).thenReturn(dailyMetrics);

        underTest.saveDailyMetrics("test", dailyMetricsDto);

        verify(clientRepository, times(1)).save(client);
        verify(weekMetricsService, times(1)).saveWeekMetrics(any());
    }

    @Test
    void testCalculateAverageWeeklyMetrics() {
        ClientEntity client = new ClientEntity();
        client.setUsername("test");
        client.setId(1L);
        client.setDailyMetrics(new ArrayList<>());

        DailyMetricsEntity dailyMetrics = new DailyMetricsEntity();
        dailyMetrics.setId(1L);
        dailyMetrics.setClient(client);
        dailyMetrics.setMood(8);
        dailyMetrics.setCaloriesIntake(2200.0);
        dailyMetrics.setEnergyLevels(7);
        dailyMetrics.setStepsCount(10000.0);
        dailyMetrics.setWeight(74.0);
        dailyMetrics.setSleepDuration(6.0);

        when(clientRepository.findByUsername("test")).thenReturn(Optional.of(client));
        when(dailyMetricsRepository.findAllByClientId(1L)).thenReturn(List.of(dailyMetrics));

        underTest.calculateAverageWeeklyMetrics("test");

        verify(dailyMetricsRepository, times(1)).findAllByClientId(1L);
    }

    @Test
    void calculcateWeightProgress_whenClientWeightIsNotNull_thenCalculate() {

        double expected = Math.floor((74.0 / 80.0) * 100);

        double actual = underTest.calculcateWeightProgress(74.0, 80.0);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void calculcateWeightProgress_whenClientWeightNull_thenCalculate() {

        double expected = 0D;

        double actual = underTest.calculcateWeightProgress(null, 80.0);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testGetAllByWeekNumber_whenWeekWithNumberExists_thenReturnListOfThem() {
        DailyMetricsEntity dailyMetrics = new DailyMetricsEntity();
        dailyMetrics.setId(1L);
        dailyMetrics.setMood(8);
        dailyMetrics.setCaloriesIntake(2200.0);
        dailyMetrics.setEnergyLevels(7);
        dailyMetrics.setStepsCount(10000.0);
        dailyMetrics.setWeight(74.0);
        dailyMetrics.setSleepDuration(6.0);

        WeekMetricsEntity weekMetrics = new WeekMetricsEntity();
        weekMetrics.setNumber(1);
        weekMetrics.setDailyMetrics(List.of(dailyMetrics));

        DailyMetricsDTO dailyMetricsDto = new DailyMetricsDTO();
        dailyMetricsDto.setMood(8);
        dailyMetricsDto.setCaloriesIntake(2200.0);
        dailyMetricsDto.setEnergyLevels(7);
        dailyMetricsDto.setStepsCount(10000.0);
        dailyMetricsDto.setWeight(74.0);
        dailyMetrics.setSleepDuration(6.0);


        when(weekMetricsService.getByNumber(1)).thenReturn(weekMetrics);
        when(modelMapper.map(dailyMetrics,  DailyMetricsDTO.class)).thenReturn(dailyMetricsDto);

        underTest.getAllByWeekNumber(1);

        verify(weekMetricsService, times(1)).getByNumber(1);
    }




}