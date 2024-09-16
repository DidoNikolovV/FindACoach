package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
import com.softuni.fitlaunch.model.entity.WeekMetricsEntity;
import com.softuni.fitlaunch.repository.WeekMetricsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeekMetricsServiceTest {

    @Mock
    private WeekMetricsRepository weekMetricsRepository;

    @Mock
    private DailyMetricsService dailyMetricsService;

    @InjectMocks
    private WeekMetricsService weekMetricsService;


    public WeekMetricsServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveWeekMetrics_whenValidWeekMetrics_thenReturnSavedEntity() {
        WeekMetricsEntity newWeekMetrics = new WeekMetricsEntity();
        when(weekMetricsRepository.save(any(WeekMetricsEntity.class))).thenReturn(newWeekMetrics);

        WeekMetricsEntity savedWeekMetrics = weekMetricsService.saveWeekMetrics(newWeekMetrics);

        assertNotNull(savedWeekMetrics);
        verify(weekMetricsRepository).save(newWeekMetrics);
    }

    @Test
    void getByNumber_whenWeekNumberExists_thenReturnWeekMetricsEntity() {
        int weekNumber = 1;
        ClientEntity client = new ClientEntity();
        client.setUsername("tetClient");
        client.setId(1L);
        WeekMetricsEntity weekMetricsEntity = new WeekMetricsEntity();
        weekMetricsEntity.setClient(client);

        when(weekMetricsRepository.findByNumber(weekNumber)).thenReturn(Optional.of(weekMetricsEntity));

        WeekMetricsEntity foundWeekMetrics = weekMetricsService.getByNumberAndClientId(weekNumber, client.getId());

        assertNotNull(foundWeekMetrics);
        assertEquals(weekMetricsEntity, foundWeekMetrics);
        verify(weekMetricsRepository).findByNumber(weekNumber);
    }

    @Test
    void getByNumber_whenWeekNumberDoesNotExist_thenReturnNull() {
        int weekNumber = 1;
        ClientEntity client = new ClientEntity();
        client.setUsername("tetClient");
        client.setId(1L);

        when(weekMetricsRepository.findByNumber(weekNumber)).thenReturn(Optional.empty());

        WeekMetricsEntity foundWeekMetrics = weekMetricsService.getByNumberAndClientId(weekNumber, client.getId());

        assertNull(foundWeekMetrics);
        verify(weekMetricsRepository).findByNumber(weekNumber);
    }

    @Test
    void getAll_whenCalled_thenReturnAllWeekMetricsEntities() {
        List<WeekMetricsEntity> weekMetricsList = Arrays.asList(new WeekMetricsEntity(), new WeekMetricsEntity());

        when(weekMetricsRepository.findAll()).thenReturn(weekMetricsList);

        List<WeekMetricsEntity> foundWeekMetricsList = weekMetricsService.getAll();

        assertNotNull(foundWeekMetricsList);
        assertEquals(2, foundWeekMetricsList.size());
        verify(weekMetricsRepository).findAll();
    }

    @Test
    void calculateAverageByClient_whenClientHasMetrics_thenReturnWeeklyAverages() {
        ClientDTO client = new ClientDTO();
        client.setId(1L);

        List<DailyMetricsEntity> clientMetrics = Arrays.asList(
                createDailyMetricsEntity(1, 70, 2000, 10000, 8,  6),
                createDailyMetricsEntity(1, 72, 2100, 11000, 7.5,  7),
                createDailyMetricsEntity(2, 71, 2200, 12000, 6.5,  5)
        );

        when(dailyMetricsService.getAllByClientId(client.getId())).thenReturn(clientMetrics);

        Map<Integer, DailyMetricsDTO> averages = weekMetricsService.calculateAverageByClient(client);

        assertNotNull(averages);
        assertEquals(2, averages.size());

        DailyMetricsDTO week1Average = averages.get(1);
        assertEquals(71.0, week1Average.getWeight());
        assertEquals(2050.0, week1Average.getCaloriesIntake());
        assertEquals(10500.0, week1Average.getStepsCount());
        assertEquals(7.0, week1Average.getSleepDuration());
        assertEquals(6, week1Average.getEnergyLevels());

        DailyMetricsDTO week2Average = averages.get(2);
        assertEquals(71.0, week2Average.getWeight());
        assertEquals(2200.0, week2Average.getCaloriesIntake());
        assertEquals(12000.0, week2Average.getStepsCount());
        assertEquals(6.0, week2Average.getSleepDuration());
        assertEquals(5, week2Average.getEnergyLevels());

        verify(dailyMetricsService).getAllByClientId(client.getId());
    }

    private DailyMetricsEntity createDailyMetricsEntity(int weekNumber, double weight, double caloriesIntake, double stepsCount, double sleepDuration, int energyLevels) {
        DailyMetricsEntity dailyMetrics = new DailyMetricsEntity();
        WeekMetricsEntity weekMetrics = new WeekMetricsEntity();
        weekMetrics.setNumber(weekNumber);
        dailyMetrics.setWeek(weekMetrics);
        dailyMetrics.setWeight(weight);
        dailyMetrics.setCaloriesIntake(caloriesIntake);
        dailyMetrics.setStepsCount(stepsCount);
        dailyMetrics.setSleepDuration(sleepDuration);
        dailyMetrics.setEnergyLevels(energyLevels);
        return dailyMetrics;
    }

}