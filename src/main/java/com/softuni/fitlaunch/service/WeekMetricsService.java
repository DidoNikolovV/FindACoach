package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.WeekMetricsEntity;
import com.softuni.fitlaunch.repository.WeekMetricsRepository;
import org.springframework.stereotype.Service;

@Service
public class WeekMetricsService {

    private final WeekMetricsRepository weekMetricsRepository;

    public WeekMetricsService(WeekMetricsRepository weekMetricsRepository) {
        this.weekMetricsRepository = weekMetricsRepository;
    }

    public WeekMetricsEntity saveWeekMetrics(WeekMetricsEntity newWeekMetrics) {
        return weekMetricsRepository.save(newWeekMetrics);
    }

    public WeekMetricsEntity getByNumber(int number) {
        return weekMetricsRepository.findByNumber(number).orElse(null);
    }
}
