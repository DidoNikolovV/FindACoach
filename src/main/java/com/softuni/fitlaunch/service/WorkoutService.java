package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import com.softuni.fitlaunch.model.dto.week.WeekDTO;
import com.softuni.fitlaunch.model.dto.workout.ClientWorkoutDetails;
import com.softuni.fitlaunch.model.dto.workout.WorkoutAddDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.entity.*;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.repository.DayWorkoutsRepository;
import com.softuni.fitlaunch.repository.ProgramRepository;
import com.softuni.fitlaunch.repository.WorkoutRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    @Value("${app.images.path}")
    private String BASE_IMAGES_PATH;
    private final WorkoutRepository workoutRepository;

    private final WorkoutExerciseService workoutExerciseService;

    private final ClientService clientService;


    private final CoachService coachService;

    private final ModelMapper modelMapper;

    private final DayWorkoutsRepository dayWorkoutsRepository;

    private final UserService userService;
    private final WeekService weekService;
    private final ProgramRepository programRepository;


    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, ClientService clientService, CoachService coachService, ModelMapper modelMapper, DayWorkoutsRepository dayWorkoutsRepository, UserService userService, WeekService weekService, ProgramRepository programRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.clientService = clientService;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
        this.dayWorkoutsRepository = dayWorkoutsRepository;
        this.userService = userService;
        this.weekService = weekService;
        this.programRepository = programRepository;
    }

    public WorkoutDTO createWorkout(WorkoutCreationDTO workoutCreationDTO, String authorUsername) {
        WorkoutEntity workout = modelMapper.map(workoutCreationDTO, WorkoutEntity.class);
        CoachEntity author = coachService.getCoachEntityByUsername(authorUsername);
        workout.setAuthor(author);
        workout = workoutRepository.save(workout);
        workoutExerciseService.createWorkoutExercises(workoutCreationDTO, workout);

        return modelMapper.map(workout, WorkoutDTO.class);
    }


    public WorkoutDTO getWorkoutById(Long id) {
        WorkoutEntity workoutEntity = workoutRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workout with id " + id + " does not exist"));
        return modelMapper.map(workoutEntity, WorkoutDTO.class);
    }

    public WorkoutEntity getWorkoutEntityById(Long id) {
        return workoutRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workout with id " + id + " does not exist"));
    }

    public DayWorkoutsEntity getDayWorkout(Long workoutId, String dayName) {
        Optional<DayWorkoutsEntity> byWorkoutIdAndName = dayWorkoutsRepository.findByNameAndWorkoutId(dayName, workoutId);
        DayWorkoutsEntity dayWorkoutsEntity = byWorkoutIdAndName.get();
        return dayWorkoutsEntity;
//        return dayWorkoutsRepository.findByWorkoutIdAndName(workoutId, dayName).orElseThrow(() -> new ResourceNotFoundException("Workout not does not exist"));
    }

    public Page<WorkoutDTO> getAllWorkouts(Pageable pageable) {
        return workoutRepository
                .findAll(pageable)
                .map(entity -> modelMapper.map(entity, WorkoutDTO.class));
    }

    public List<WorkoutDTO> getAllWorkouts() {
        return workoutRepository
                .findAll().stream().map(entity -> modelMapper.map(entity, WorkoutDTO.class)).toList();
    }

    public List<WorkoutAddDTO> getAllWorkoutNames() {
        return workoutRepository
                .findAll().stream().map(entity -> modelMapper.map(entity, WorkoutAddDTO.class)).toList();
    }

    public List<LevelEnum> getAllLevels() {
        return Arrays.stream(LevelEnum.values()).collect(Collectors.toList());
    }

    public WorkoutDetailsDTO getWorkoutDetailsById(Long workoutId, String dayName) {
        WorkoutEntity workout = workoutRepository.findById(workoutId).orElse(null);

        WorkoutDetailsDTO map = modelMapper.map(workout, WorkoutDetailsDTO.class);
        return map;
    }

    public ClientWorkoutDetails getWorkoutDetailsByIdForClient(Long workoutId, String clientName, String dayName) {
        DayWorkoutsEntity workout = dayWorkoutsRepository.findByNameAndWorkoutId(dayName, workoutId).orElse(null);
        UserEntity client = userService.getUserEntityByUsername(clientName);
        DayWorkoutsEntity dayWorkoutsEntity = client.getCompletedWorkouts().stream().filter(completedWorkout -> completedWorkout.equals(workout)).toList().get(0);
        return modelMapper.map(dayWorkoutsEntity, ClientWorkoutDetails.class);
    }

    public void startWorkout(Long workoutId, String username, String dayName) {
        UserEntity user = userService.getUserEntityByUsername(username);

        DayWorkoutsEntity dayWorkout = getDayWorkout(workoutId, dayName);

        boolean isStarted = isWorkoutStarted(workoutId, dayName, username);
        if (!isStarted) {
            user.getStartedWorkouts().add(dayWorkout);
            dayWorkout.setStarted(true);
        }

        userService.saveUser(user);
    }

    public void completedWorkout(Long workoutId, String username, String dayName) {
        UserEntity user = userService.getUserEntityByUsername(username);

        DayWorkoutsEntity dayWorkout = getDayWorkout(workoutId, dayName);

        boolean isCompleted = isWorkoutCompleted(workoutId, dayName, username);
        if (!isCompleted) {
            user.getCompletedWorkouts().add(dayWorkout);
            dayWorkout.setCompleted(true);
        }

        userService.saveUser(user);
    }

    public boolean isWorkoutStarted(Long workoutId, String dayName, String username) {
        DayWorkoutsEntity dayWorkoutsEntity = getDayWorkout(workoutId, dayName);
        UserEntity user = userService.getUserEntityByUsername(username);
        return user.getStartedWorkouts().stream().anyMatch(workout -> workout.getId().equals(dayWorkoutsEntity.getId()));
    }

    public boolean isWorkoutCompleted(Long workoutId, String dayName, String username) {
        DayWorkoutsEntity dayWorkoutsEntity = getDayWorkout(workoutId, dayName);
        UserEntity user = userService.getUserEntityByUsername(username);
        return user.getCompletedWorkouts().stream().anyMatch(workout -> workout.getId().equals(dayWorkoutsEntity.getId()));
    }


    public void like(Long workoutId, String username) {
        UserEntity user = userService.getUserEntityByUsername(username);

        boolean hasLiked = user.getWorkoutsLiked().stream().anyMatch(workoutEntity -> Objects.equals(workoutEntity.getId(), workoutId));

        if (!hasLiked) {
            WorkoutEntity workout = getWorkoutEntityById(workoutId);
            user.getWorkoutsLiked().add(workout);
            int oldLikes = workout.getLikes();
            workout.setLikes(oldLikes + 1);
            userService.saveUser(user);
        }

    }

    public void dislike(Long workoutId, String username) {
        UserEntity user = userService.getUserEntityByUsername(username);

        boolean hasLiked = user.getWorkoutsLiked().stream().anyMatch(workoutEntity -> Objects.equals(workoutEntity.getId(), workoutId));

        if (hasLiked) {
            WorkoutEntity workout = getWorkoutEntityById(workoutId);
            user.getWorkoutsLiked().remove(workout);
            int oldLikes = workout.getLikes();
            workout.setLikes(oldLikes - 1);
            userService.saveUser(user);
        }

    }

    public void completeExercise(Long workoutId, String dayName, Long exerciseId) {
       workoutExerciseService.completeExercise(exerciseId, workoutId);
    }


    public List<DayWorkoutsDTO> getAllByWorkoutIds(Long programId, int week, List<WorkoutAddDTO> workoutAddDTO) {
        ProgramEntity programEntity = programRepository.findById(programId).orElse(null);
        ProgramWeekEntity weekEntity = weekService.getWeekByWeekNumberAndProgramId(week, programId);
        List<Long> ids = workoutAddDTO.stream().map(WorkoutAddDTO::getId).toList();
        List<WorkoutEntity> workouts = ids.stream().map(id -> workoutRepository.findById(id).get()).toList();
        List<DayWorkoutsEntity> daysWorkouts = workouts.stream().map(workout -> createDayWorkout(workout, weekEntity)).toList();
        List<DayWorkoutsDTO> daysWorkoutsDTO = daysWorkouts.stream().map(dayWorkoutsEntity -> modelMapper.map(dayWorkoutsEntity, DayWorkoutsDTO.class)).toList();
        return daysWorkoutsDTO;
    }

    private DayWorkoutsEntity createDayWorkout(WorkoutEntity workoutEntity, ProgramWeekEntity weekEntity) {
        DayWorkoutsEntity dayWorkoutsEntity = new DayWorkoutsEntity();
        dayWorkoutsEntity.setName(workoutEntity.getName());
        dayWorkoutsEntity.setWorkout(workoutEntity);
        dayWorkoutsEntity.setWeek(weekEntity);
        dayWorkoutsEntity.setStarted(false);
        dayWorkoutsEntity.setCompleted(false);

        dayWorkoutsRepository.save(dayWorkoutsEntity);

        return dayWorkoutsEntity;
    }
}
