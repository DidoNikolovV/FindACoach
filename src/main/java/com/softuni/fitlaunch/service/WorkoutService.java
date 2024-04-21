package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
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


    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, ClientService clientService, CoachService coachService, ModelMapper modelMapper, DayWorkoutsRepository dayWorkoutsRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.clientService = clientService;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
        this.dayWorkoutsRepository = dayWorkoutsRepository;
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

    public DayWorkoutsEntity getDayWorkout(Long workoutId, Long weekId, String dayName) {
        return dayWorkoutsRepository.findByWorkoutIdAndWeekIdAndName(workoutId, weekId, dayName).orElseThrow(() -> new ResourceNotFoundException("Workout not does not exist"));
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

    public void startWorkout(Long workoutId, String username, Long weekId, String dayName) {
        WorkoutEntity workout = getWorkoutEntityById(workoutId);
        ClientEntity client = clientService.getClientEntityByUsername(username);

        DayWorkoutsEntity dayWorkout = getDayWorkout(workoutId, weekId, dayName);

        boolean isStarted = isWorkoutStarted(dayWorkout.getId(), username);
        if (!isStarted) {
            client.getStartedWorkouts().add(dayWorkout);
        }

        dayWorkoutsRepository.save(dayWorkout);
    }

    public void completedWorkout(Long workoutId, String username, Long weekId, String dayName) {
        WorkoutEntity workout = getWorkoutEntityById(workoutId);
        ClientEntity client = clientService.getClientEntityByUsername(username);

        DayWorkoutsEntity dayWorkout = getDayWorkout(workoutId, weekId, dayName);

        boolean isCompleted = isWorkoutCompleted(dayWorkout.getId(), username);
        if (!isCompleted) {
            client.getCompletedWorkouts().add(dayWorkout);
        }

        dayWorkoutsRepository.save(dayWorkout);
    }

    public boolean isWorkoutStarted(Long workoutId, String username) {
        DayWorkoutsEntity dayWorkoutsEntity = dayWorkoutsRepository.findById(workoutId).orElseThrow(() -> new ResourceNotFoundException("Workout does not exist"));
        ClientEntity client = clientService.getClientEntityByUsername(username);
        return client.getStartedWorkouts().stream().anyMatch(workout -> workout.getId().equals(dayWorkoutsEntity.getId()));
    }

    public boolean isWorkoutCompleted(Long workoutId, String username) {
        DayWorkoutsEntity dayWorkoutsEntity = dayWorkoutsRepository.findById(workoutId).orElseThrow(() -> new ResourceNotFoundException("Workout does not exist"));
        ClientEntity client = clientService.getClientEntityByUsername(username);
        return client.getCompletedWorkouts().stream().anyMatch(workout -> workout.getId().equals(dayWorkoutsEntity.getId()));
    }
}
