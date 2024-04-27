package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.week.DayCreationDTO;
import com.softuni.fitlaunch.model.dto.week.DayDTO;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.repository.DayRepository;
import com.softuni.fitlaunch.repository.WeekRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public ProgramWeekEntity getWeekByProgramId(Long programId, Long id) {
        List<ProgramWeekEntity> allByProgramId = weekRepository.findAllByProgramId(programId);
        return allByProgramId.stream().filter(weekEntity -> weekEntity.getId().equals(id)).toList().get(0);
    }

    public ProgramWeekEntity getWeekByNumber(int number, Long programId) {
        return weekRepository.findByNumberAndProgramId(number, programId).orElseThrow(() -> new ResourceNotFoundException("Week number " + number + " does not exist"));
    }

    public void addAll(List<ProgramWeekEntity> weeks) {
        weekRepository.saveAll(weeks);
    }

    public List<DayCreationDTO> getAllDays() {
        return dayRepository.findAll().stream().map(day -> modelMapper.map(day, DayCreationDTO.class)).toList();
    }

    public void updateDaysForWeek(ProgramWeekEntity week, List<DayWorkoutsEntity> daysToBeUpdated) {
        Map<String, DayWorkoutsEntity> dayMap = week.getDays().stream()
                .collect(Collectors.toMap(DayWorkoutsEntity::getName, day -> day));

        for (DayWorkoutsEntity updatedDay : daysToBeUpdated) {
            DayWorkoutsEntity weekDay = dayMap.get(updatedDay.getName());
            if (weekDay != null) {
                weekDay.setWorkout(updatedDay.getWorkout());
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
