package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
import com.softuni.fitlaunch.model.entity.WeekMetricsEntity;
import com.softuni.fitlaunch.repository.WeekMetricsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeekMetricsService {

    private final WeekMetricsRepository weekMetricsRepository;

    private final DailyMetricsService dailyMetricsService;

    public WeekMetricsService(WeekMetricsRepository weekMetricsRepository, DailyMetricsService dailyMetricsService) {
        this.weekMetricsRepository = weekMetricsRepository;
        this.dailyMetricsService = dailyMetricsService;
    }

    public WeekMetricsEntity saveWeekMetrics(WeekMetricsEntity newWeekMetrics) {
        return weekMetricsRepository.save(newWeekMetrics);
    }

    public WeekMetricsEntity getByNumber(int number) {
        return weekMetricsRepository.findByNumber(number).orElse(null);
    }

    public List<WeekMetricsEntity> getAll() {
        return weekMetricsRepository.findAll();
    }

    public Map<Integer, DailyMetricsDTO> calculateAverageByClient(ClientDTO client) {
        List<DailyMetricsEntity> clientMetrics = dailyMetricsService.getAllByClientId(client.getId());
        Map<Integer, List<DailyMetricsEntity>> averageForWeek = new HashMap<>();

        for (DailyMetricsEntity data : clientMetrics) {
            int weekNumber = data.getWeek().getNumber();
            averageForWeek.computeIfAbsent(weekNumber, k -> new ArrayList<>()).add(data);
        }

        Map<Integer, DailyMetricsDTO> weeklyAverages = new HashMap<>();
        for (Map.Entry<Integer, List<DailyMetricsEntity>> entry : averageForWeek.entrySet()) {
            int weekNumber = entry.getKey();
            List<DailyMetricsEntity> weekData = entry.getValue();

            double totalWeight = 0;
            double totalCaloriesIntake = 0;
            double totalStepsCount = 0;
            double totalSleepDuration = 0;
            int totalMood = 0;
            int totalEnergyLevels = 0;

            int count = weekData.size();
            for (DailyMetricsEntity data : weekData) {
                totalWeight += data.getWeight();
                totalCaloriesIntake += data.getCaloriesIntake();
                totalStepsCount += data.getStepsCount();
                totalSleepDuration += data.getSleepDuration();
                totalEnergyLevels += data.getEnergyLevels();
            }


            DailyMetricsDTO average = new DailyMetricsDTO();
            average.setWeight(Math.floor(totalWeight / count));
            average.setCaloriesIntake(Math.floor(totalCaloriesIntake / count));
            average.setStepsCount(Math.floor(totalStepsCount / count));
            average.setSleepDuration(Math.floor(totalSleepDuration / count));
            average.setEnergyLevels(totalEnergyLevels / count);

            weeklyAverages.put(weekNumber, average);
        }

        return weeklyAverages;
    }
}
