package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.program.ProgramCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.week.WeekCreationDTO;
import com.softuni.fitlaunch.model.dto.week.WeekDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DayEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.WeekEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.DaysEnum;
import com.softuni.fitlaunch.repository.DayRepository;
import com.softuni.fitlaunch.repository.ProgramRepository;
import com.softuni.fitlaunch.repository.WeekRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    private final WorkoutService workoutService;

    private final CoachService coachService;

    private final ModelMapper modelMapper;

    private final DayRepository dayRepository;

    private final WeekService weekService;


    public ProgramService(ProgramRepository programRepository, WorkoutService workoutService, CoachService coachService, ModelMapper modelMapper, DayRepository dayRepository, WeekService weekService) {
        this.programRepository = programRepository;
        this.workoutService = workoutService;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
        this.dayRepository = dayRepository;
        this.weekService = weekService;
    }

    public List<ProgramDTO> loadAllProgramsByCoachId(Long coachId) {
        List<ProgramEntity> programEntities = programRepository.findAllByCoachId(coachId).orElseThrow(() -> new ResourceNotFoundException("Programs with coachId " + coachId + " not found"));
        return programEntities.stream().map(programEntity -> modelMapper.map(programEntity, ProgramDTO.class)).toList();
    }


    public WorkoutEntity getWorkoutEntityById(Long id) {
        return workoutService.getWorkoutEntityById(id);
    }

    public List<WeekEntity> getAllWeeksByProgramId(Long programId) {
        ProgramEntity programEntity = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " does not exist"));
        return programEntity.getWeeks();
    }


    public ProgramDTO getById(Long programId) {
        ProgramEntity programEntity = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " not found"));
        return modelMapper.map(programEntity, ProgramDTO.class);
    }

    public ProgramDTO getProgramById(Long programId) {
        ProgramEntity program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " not found"));
        return modelMapper.map(program, ProgramDTO.class);
    }

    public ProgramEntity getProgramEntityById(Long programId) {
        return programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " not found"));
    }

    public void removeLike(WorkoutEntity weekWorkout) {
        Long oldLikes = Long.valueOf(weekWorkout.getLikes());
        if (oldLikes - 1 < 0) {
            weekWorkout.setLikes(0);
        } else {
            weekWorkout.setLikes((int) (oldLikes - 1));
        }
    }

    public ProgramEntity createProgram(ProgramCreationDTO programCreationDTO, String username) {
        CoachEntity coach = coachService.getCoachEntityByUsername(username);
        ProgramEntity program = modelMapper.map(programCreationDTO, ProgramEntity.class);
        List<WeekEntity> weeks = new ArrayList<>();

        for(int i = 0; i < programCreationDTO.getWeeks(); i++) {
            WeekEntity newWeek = new WeekEntity();
            newWeek.setNumber(i + 1);
            weeks.add(newWeek);
        }

        for (WeekEntity week : weeks) {
            week.setProgram(program);
            for(int i = 1; i <= 7; i++) {
                DayEntity day = new DayEntity();
                DaysEnum dayEnum = DaysEnum.values()[i - 1];
                day.setName(dayEnum.name());
                day.setWeek(week);
                week.getDays().add(day);
            }
        }

        program.setCoach(coach);
        program.setImgUrl("/images/intermediate-program.jpg");
        program.setWeeks(weeks);
        program = programRepository.save(program);

        return program;
    }

    public void addWeekWithWorkouts(WeekCreationDTO weekCreationDTO) {
        WorkoutEntity workoutToBeAdded = workoutService.getWorkoutEntityById(weekCreationDTO.getWorkoutId());
        WeekEntity week = weekService.getWeekByNumber(weekCreationDTO.getNumber());
        List<DayEntity> daysToBeUpdated = new ArrayList<>();
        for (String day : weekCreationDTO.getDays()) {
            DayEntity dayEntity = new DayEntity();
            dayEntity.setWeek(week);
            dayEntity.getWorkouts().add(workoutToBeAdded);
            dayEntity.setName(day);
            daysToBeUpdated.add(dayEntity);
        }

        weekService.updateDaysForWeek(week, daysToBeUpdated);
    }

    public ProgramWeekDTO getWeekById(int weekId) {
        WeekEntity week = weekService.getWeekByNumber(weekId);
        ProgramWeekDTO programWeek = modelMapper.map(week, ProgramWeekDTO.class);
        return programWeek;
    }

//    public List<WeekEntity> getWeeksWithoutInformation(Long programId) {
//        ProgramEntity program = getProgramEntityById(programId);
//
//    }
}
