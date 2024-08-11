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
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
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

            // Create or update UserProgress for each workout in the week
            for (DayWorkoutsEntity dayWorkout : week.getDays()) {
                UserProgress userProgress = userProgressService.getOrCreateUserProgress(user, program, week, dayWorkout);
                // Optionally, update progress status (e.g., started, completed) based on user actions
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


    public void updateWeeksStateByUser(ProgramEntity program, UserEntity user) {
        ProgramDTO programDTO = modelMapper.map(program, ProgramDTO.class);

        List<UserProgress> userProgressList = userProgressService.getUserProgressForProgram(user.getUsername(), program.getId());

        for (ProgramWeekEntity week : program.getWeeks()) {
            ProgramWeekDTO weekDto = modelMapper.map(week, ProgramWeekDTO.class);
            weekDto.setCompleted(isWeekCompleted(userProgressList, week));
            programDTO.getWeeks().add(weekDto);
        }
    }

    private boolean isWeekCompleted(List<UserProgress> userProgressList, ProgramWeekEntity week) {
        return userProgressList.stream()
                .filter(progress -> progress.getWeek().equals(week))
                .allMatch(UserProgress::isWeekCompleted);
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

    public void updateDaysWorkoutState(Long weekId, Long programId, String username) {
        UserEntity user = userService.getUserEntityByUsername(username);
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);
        week.getDays().forEach(workout -> {
            updatedWorkoutState(workout, user);
        });

    }

    private void updatedWorkoutState(DayWorkoutsEntity workout, UserEntity user) {
        UserProgress userProgress = userProgressService.getByUserIdAndWorkoutId(user.getId(), workout.getId());
        boolean hasNotCompleted = !userProgress.isWorkoutCompleted();
        if (hasNotCompleted) {
            userProgress.setWorkoutCompleted(false);
        }
    }

    public ProgramWeekDTO getWeekById(Long weekId, Long programId, String username) {
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);
        UserEntity user = userService.getUserEntityByUsername(username);
        List<UserProgress> userProgressList = userProgressService.getUserProgressForProgramIdAndWeekId(user.getId(), programId, weekId);

        ProgramWeekDTO weekDTO = modelMapper.map(week, ProgramWeekDTO.class);

        List<DayWorkoutsDTO> dayDTOs = week.getDays().stream()
                .map(day -> {
                    DayWorkoutsDTO dayDTO = modelMapper.map(day, DayWorkoutsDTO.class);

                    // Find the corresponding UserProgress record
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

        // Set the days in the weekDTO
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
