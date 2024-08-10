package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.program.ProgramCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProgress;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.DaysEnum;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.repository.ProgramRepository;
import com.softuni.fitlaunch.repository.UserProgressRepository;
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

    private final UserService userService;

    private final ClientService clientService;
    private final UserProgressRepository userProgressRepository;


    public ProgramService(ProgramRepository programRepository, CoachService coachService, ModelMapper modelMapper, WeekService weekService, WorkoutService workoutService, UserService userService, ClientService clientService, UserProgressRepository userProgressRepository) {
        this.programRepository = programRepository;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
        this.weekService = weekService;
        this.workoutService = workoutService;
        this.userService = userService;
        this.clientService = clientService;
        this.userProgressRepository = userProgressRepository;
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
        ProgramEntity program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program with id " + programId + " not found"));
        UserEntity user = userService.getUserEntityByUsername(username);
//        updateWeeksStateByUser(program, user);
        return modelMapper.map(program, ProgramDTO.class);
    }

//    public void updateWeeksStateByUser(ProgramEntity programEntity, UserEntity user) {
//        programEntity.getWeeks().forEach(week -> {
//            UserProgress userProgress = userProgressRepository.findByUserIdAndWeekId(user.getId(), week.getId()).orElse(null);
//            boolean hasNotCompleted = userProgress.isWeekCompleted();
//            if (hasNotCompleted) {
//                week.setCompleted(false);
//            }
//        });
//    }

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

        return program;
    }

    public void updateDaysWorkoutState(Long weekId, Long programId, String username) {
        UserEntity user = userService.getUserEntityByUsername(username);
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);
        week.getDays().forEach(workout -> {
            updatedWorkoutState(workout, user);
        });

    }

    private void updatedWorkoutState(DayWorkoutsEntity workout, UserEntity user) {
        UserProgress userProgress = userProgressRepository.findByUserIdAndWorkoutId(user.getId(), workout.getId()).orElse(null);
        boolean hasNotCompleted = !userProgress.isWorkoutCompleted();
        if (hasNotCompleted) {
            workout.setCompleted(false);
        }
    }

    public ProgramWeekDTO getWeekById(Long weekId, Long programId, String username) {
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);
        updateDaysWorkoutState(weekId, programId, username);
        return modelMapper.map(week, ProgramWeekDTO.class);
    }


    public void completeWeek(Long weekNumber, Long programId, String name) {
        ProgramWeekEntity programWeek = weekService.getWeekByNumber(weekNumber, programId);
        UserEntity user = userService.getUserEntityByUsername(name);
        UserProgress userProgress = new UserProgress();
        userProgress.setWeek(programWeek);
        userProgress.setUser(user);
        userProgress.setWeekCompleted(true);
        userProgressRepository.save(userProgress);
        userService.saveUser(user);
    }
}
