package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.repository.WeekRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WeekService {

    private final WeekRepository weekRepository;

    public WeekService(WeekRepository weekRepository) {
        this.weekRepository = weekRepository;
    }

    public ProgramWeekEntity getWeekByNumber(Long number, Long programId) {
        return weekRepository.findByNumberAndProgramId(number, programId).orElseThrow(() -> new ResourceNotFoundException("Week number " + number + " does not exist"));
    }
}
