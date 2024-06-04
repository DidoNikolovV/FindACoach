package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
import com.softuni.fitlaunch.repository.DailyMetricsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyMetricsService {

    private final DailyMetricsRepository dailyMetricsRepository;

    public DailyMetricsService(DailyMetricsRepository dailyMetricsRepository) {
        this.dailyMetricsRepository = dailyMetricsRepository;
    }

    public List<DailyMetricsEntity> getAllByClientId(Long id) {
        return dailyMetricsRepository.findAllByClientId(id);
    }
}
