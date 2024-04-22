package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.program.ProgramCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.week.WeekCreationDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.DaysEnum;
import com.softuni.fitlaunch.repository.ProgramRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;


    private final CoachService coachService;

    private final ModelMapper modelMapper;

    private final WeekService weekService;

    private final WorkoutService workoutService;

    public ProgramService(ProgramRepository programRepository, CoachService coachService, ModelMapper modelMapper, WeekService weekService, WorkoutService workoutService) {
        this.programRepository = programRepository;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
        this.weekService = weekService;
        this.workoutService = workoutService;
    }

    public List<ProgramDTO> loadAllProgramsByCoachId(Long coachId) {
        List<ProgramEntity> programEntities = programRepository.findAllByCoachId(coachId).orElseThrow(() -> new ResourceNotFoundException("Programs with coachId " + coachId + " not found"));
        return programEntities.stream().map(programEntity -> modelMapper.map(programEntity, ProgramDTO.class)).toList();
    }

    public List<ProgramWeekEntity> getAllWeeksByProgramId(Long programId) {
        ProgramEntity programEntity = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " does not exist"));
        return programEntity.getWeeks();
    }


    public ProgramDTO getById(Long programId) {
        ProgramEntity programEntity = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " not found"));
        return modelMapper.map(programEntity, ProgramDTO.class);
    }

    public ProgramDTO getProgramById(Long programId) {
        ProgramEntity program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " not found"));
        ProgramDTO map = modelMapper.map(program, ProgramDTO.class);
        return map;
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
        List<ProgramWeekEntity> weeks = new ArrayList<>();

        for(int i = 0; i < programCreationDTO.getWeeks(); i++) {
            ProgramWeekEntity newWeek = new ProgramWeekEntity();
            newWeek.setNumber(i + 1);
            weeks.add(newWeek);
        }

        for (ProgramWeekEntity week : weeks) {
            week.setProgram(program);
            for(int i = 1; i <= 7; i++) {
                DayWorkoutsEntity day = new DayWorkoutsEntity();
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



    public ProgramWeekDTO getWeekById(int weekId) {
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId);
        ProgramWeekDTO programWeek = modelMapper.map(week, ProgramWeekDTO.class);
        return programWeek;
    }

    public void addWeekWithWorkouts(WeekCreationDTO weekCreationDTO) {
        WorkoutEntity workoutToBeAdded = workoutService.getWorkoutEntityById(weekCreationDTO.getWorkoutId());
        ProgramWeekEntity week = weekService.getWeekByNumber(weekCreationDTO.getNumber());
        List<DayWorkoutsEntity> daysToBeUpdated = new ArrayList<>();

        for (String day : weekCreationDTO.getDays()) {
            DayWorkoutsEntity dayWorkoutsEntity = new DayWorkoutsEntity();
            dayWorkoutsEntity.setWorkout(workoutToBeAdded);
            dayWorkoutsEntity.setWeek(week);
            dayWorkoutsEntity.setName(day);
            daysToBeUpdated.add(dayWorkoutsEntity);
        }

        weekService.updateDaysForWeek(week, daysToBeUpdated);
    }

//    public List<WeekEntity> getWeeksWithoutInformation(Long programId) {
//        ProgramEntity program = getProgramEntityById(programId);
//
//    }
}
