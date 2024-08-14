package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.program.ProgramCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProgress;
import com.softuni.fitlaunch.model.enums.DaysEnum;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.repository.ProgramRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    private final CoachService coachService;

    private final ModelMapper modelMapper;

    private final WeekService weekService;

    private final WorkoutService workoutService;

    private final UserService userService;

    private final ClientService clientService;
    private final UserProgressService userProgressService;


    public ProgramService(ProgramRepository programRepository, CoachService coachService, ModelMapper modelMapper, WeekService weekService, WorkoutService workoutService, UserService userService, ClientService clientService, UserProgressService userProgressService) {
        this.programRepository = programRepository;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
        this.weekService = weekService;
        this.workoutService = workoutService;
        this.userService = userService;
        this.clientService = clientService;
        this.userProgressService = userProgressService;
    }

    public List<ProgramDTO> loadAllPrograms(String username) {
        UserDTO loggedUser = userService.getUserByUsername(username);
        List<ProgramEntity> programEntities = new ArrayList<>();
        if (loggedUser.getTitle().equals(UserTitleEnum.CLIENT)) {
            ClientDTO clientDTO = clientService.getClientByUsername(loggedUser.getUsername());
            CoachDTO coach = coachService.getCoachById(clientDTO.getCoach().getId());
            programEntities = programRepository.findAllByCoachId(coach.getId()).orElseThrow(() -> new ResourceNotFoundException("Programs with coachId " + coach.getId() + " not found"));
        } else {
            CoachEntity coach = coachService.getCoachEntityByUsername(username);
            programEntities = coach.getPrograms();
        }

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

    public ProgramDTO getProgramById(Long programId, String username) {
        ProgramEntity program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " not found"));

        UserEntity user = userService.getUserEntityByUsername(username);
        List<UserProgress> userProgressList = userProgressService.getUserProgressForProgram(username, programId);

        ProgramDTO programDTO = modelMapper.map(program, ProgramDTO.class);
        List<ProgramWeekDTO> weeksDTO = new ArrayList<>();
        int totalWeeks = program.getWeeks().size();
        long completedWeeksCount = 0;

        for (ProgramWeekEntity week : program.getWeeks()) {
            ProgramWeekDTO weekDTO = modelMapper.map(week, ProgramWeekDTO.class);
            boolean isCompleted = isWeekCompleted(userProgressList, week);
            weekDTO.setCompleted(isCompleted);

            for (DayWorkoutsEntity dayWorkout : week.getDays()) {
                UserProgress userProgress = userProgressService.getOrCreateUserProgress(user, program, week, dayWorkout);
            }

            weeksDTO.add(weekDTO);

            if (isCompleted) {
                completedWeeksCount++;
            }
        }

        programDTO.setWeeks(weeksDTO);
        int completedPercentage = (totalWeeks == 0) ? 0 : (int) ((completedWeeksCount / (double) totalWeeks) * 100);
        programDTO.setCompletedPercentage(completedPercentage);

        return programDTO;
    }


    private boolean isWeekCompleted(List<UserProgress> userProgressList, ProgramWeekEntity week) {
        return userProgressList.stream()
                .filter(progress -> progress.getWeek().equals(week))
                .allMatch(UserProgress::isWeekCompleted);
    }


    public ProgramEntity createProgram(ProgramCreationDTO programCreationDTO, String username) {
        CoachEntity coach = coachService.getCoachEntityByUsername(username);
        ProgramEntity program = modelMapper.map(programCreationDTO, ProgramEntity.class);
        List<ProgramWeekEntity> weeks = new ArrayList<>();

        for (int i = 0; i < programCreationDTO.getWeeks(); i++) {
            ProgramWeekEntity newWeek = new ProgramWeekEntity();
            newWeek.setNumber(i + 1);
            weeks.add(newWeek);
        }

        for (ProgramWeekEntity week : weeks) {
            week.setProgram(program);
            for (int i = 1; i <= 7; i++) {
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
        userProgressService.saveUserProgress(program, username, weeks);

        return program;
    }


    public ProgramWeekDTO getWeekById(Long weekId, Long programId, String username) {
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);
        UserEntity user = userService.getUserEntityByUsername(username);
        List<UserProgress> userProgressList = userProgressService.getUserProgressForProgramIdAndWeekId(user.getId(), programId, weekId);

        ProgramWeekDTO weekDTO = modelMapper.map(week, ProgramWeekDTO.class);

        List<DayWorkoutsDTO> dayDTOs = week.getDays().stream()
                .map(day -> {
                    DayWorkoutsDTO dayDTO = modelMapper.map(day, DayWorkoutsDTO.class);

                    UserProgress progress = userProgressList.stream()
                            .filter(p -> p.getWorkout().getId().equals(day.getWorkout().getId()) && p.getWorkout().getName().equals(day.getName()))
                            .findFirst()
                            .orElse(null);

                    if (progress != null) {
                        dayDTO.setCompleted(progress.isWorkoutCompleted());
                        dayDTO.setStarted(progress.isWorkoutStarted());
                    }

                    return dayDTO;
                })
                .collect(Collectors.toList());

        weekDTO.setDays(dayDTOs);
        return weekDTO;
    }


    public void completeWeek(Long weekNumber, Long programId, String name) {
        ProgramWeekEntity programWeek = weekService.getWeekByNumber(weekNumber, programId);
        UserEntity user = userService.getUserEntityByUsername(name);
        userProgressService.completeWeek(programWeek, user);
        userService.saveUser(user);
    }
}
