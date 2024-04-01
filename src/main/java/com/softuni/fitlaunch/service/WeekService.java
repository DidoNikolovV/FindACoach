package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.week.DayCreationDTO;
import com.softuni.fitlaunch.model.entity.WeekEntity;
import com.softuni.fitlaunch.repository.DayRepository;
import com.softuni.fitlaunch.repository.WeekRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeekService {

    private final WeekRepository weekRepository;

    private final DayRepository dayRepository;

    private final ModelMapper modelMapper;

    public WeekService(WeekRepository weekRepository, DayRepository dayRepository, ModelMapper modelMapper) {
        this.weekRepository = weekRepository;
        this.dayRepository = dayRepository;
        this.modelMapper = modelMapper;
    }

    public WeekEntity getWeekByProgramId(Long programId, Long id) {
        List<WeekEntity> allByProgramId = weekRepository.findAllByProgramId(programId);
        return allByProgramId.stream().filter(weekEntity -> weekEntity.getId().equals(id)).toList().get(0);
    }

    public void addAll(List<WeekEntity> weeks) {
        weekRepository.saveAll(weeks);
    }

    public List<DayCreationDTO> getAllDays() {
        return dayRepository.findAll().stream().map(day -> modelMapper.map(day, DayCreationDTO.class)).toList();
    }

}
