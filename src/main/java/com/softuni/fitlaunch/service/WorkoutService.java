package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.repository.DayWorkoutsRepository;
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


    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, ClientService clientService, CoachService coachService, ModelMapper modelMapper, DayWorkoutsRepository dayWorkoutsRepository, UserService userService, WeekService weekService) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.clientService = clientService;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
        this.dayWorkoutsRepository = dayWorkoutsRepository;
        this.userService = userService;
        this.weekService = weekService;
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

    public List<LevelEnum> getAllLevels() {
        return Arrays.stream(LevelEnum.values()).collect(Collectors.toList());
    }

    public WorkoutDetailsDTO getWorkoutDetailsById(Long workoutId, String dayName) {
        WorkoutEntity workout = workoutRepository.findById(workoutId).orElse(null);

        WorkoutDetailsDTO map = modelMapper.map(workout, WorkoutDetailsDTO.class);
        return map;
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
}
