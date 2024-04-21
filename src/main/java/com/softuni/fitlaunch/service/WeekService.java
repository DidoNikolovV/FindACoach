package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.week.DayCreationDTO;
import com.softuni.fitlaunch.model.dto.week.DayDTO;
import com.softuni.fitlaunch.model.dto.week.WeekDTO;
import com.softuni.fitlaunch.model.entity.DayEntity;
import com.softuni.fitlaunch.model.entity.WeekEntity;
import com.softuni.fitlaunch.repository.DayRepository;
import com.softuni.fitlaunch.repository.WeekRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.SetUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public WeekEntity getWeekByNumber(int number) {
        return  weekRepository.findByNumber(number).orElseThrow(() -> new ResourceNotFoundException("Week number " + number + " does not exist"));
    }

    public void addAll(List<WeekEntity> weeks) {
        weekRepository.saveAll(weeks);
    }

    public List<DayCreationDTO> getAllDays() {
        return dayRepository.findAll().stream().map(day -> modelMapper.map(day, DayCreationDTO.class)).toList();
    }

    public void updateDaysForWeek(WeekEntity week, List<DayEntity> daysToBeUpdated) {
        Map<String, DayEntity> dayMap = week.getDays().stream()
                .collect(Collectors.toMap(DayEntity::getName, day -> day));

        for (DayEntity updatedDay : daysToBeUpdated) {
            DayEntity weekDay = dayMap.get(updatedDay.getName());
            if (weekDay != null) {
                weekDay.getWorkouts().addAll(updatedDay.getWorkouts());
            }
        }

        weekRepository.save(week);
    }

    public List<DayDTO> getAllDaysByWeekId(Long weekId) {
        return dayRepository.findAllByWeekId(weekId).stream().map(day -> modelMapper.map(day, DayDTO.class)).toList();
    }

//    public List<WeekDTO> getAllWeeksByProgramId(Long programId) {
//        return weekRepository.findAllByProgramId(programId).stream().map(week -> modelMapper.map(week, WeekDTO.class))
//    }

}
