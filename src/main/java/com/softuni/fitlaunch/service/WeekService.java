package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.WeekEntity;
import com.softuni.fitlaunch.repository.WeekRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeekService {

    private final WeekRepository weekRepository;

    public WeekService(WeekRepository weekRepository) {
        this.weekRepository = weekRepository;
    }

    public WeekEntity getWeekByProgramId(Long programId, Long id) {
        List<WeekEntity> allByProgramId = weekRepository.findAllByProgramId(programId);
        return allByProgramId.stream().filter(weekEntity -> weekEntity.getId().equals(id)).toList().get(0);
    }

    public void addAll(List<WeekEntity> weeks) {
        weekRepository.saveAll(weeks);
    }
}
