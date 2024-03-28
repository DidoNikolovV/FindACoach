package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.repository.WorkoutRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
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


    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, ClientService clientService, CoachService coachService, ModelMapper modelMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.clientService = clientService;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
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
        WorkoutEntity workoutEntity = workoutRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Workout with id " + id + " does not exist"));
        return modelMapper.map(workoutEntity, WorkoutDTO.class);
    }

    public WorkoutEntity getWorkoutEntityById(Long id) {
        return workoutRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Workout with id " + id + " does not exist"));
    }

    public WorkoutCreationDTO getWorkoutCreation(Long id) {
        WorkoutEntity workout = workoutRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Workout with id " + id + " does not exist"));
        return modelMapper.map(workout, WorkoutCreationDTO.class);
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

//    public List<WorkoutDTO> loadAllByProgramId(Long programId) {
//        return workoutRepository
//                .findAllByProgramId(programId)
//                .stream()
//                .map(workout -> modelMapper.map(workout, WorkoutDTO.class))
//                .toList();
//    }

    public List<LevelEnum> getAllLevels() {
        return Arrays.stream(LevelEnum.values()).collect(Collectors.toList());
    }

    public WorkoutDetailsDTO getWorkoutDetailsById(Long workoutId) {
        WorkoutEntity workout = workoutRepository.findById(workoutId).orElse(null);
        WorkoutDetailsDTO map = modelMapper.map(workout, WorkoutDetailsDTO.class);
        return map;
    }

    public void startWorkout(Long workoutId, String username) {
        WorkoutEntity workout = getWorkoutEntityById(workoutId);
        ClientEntity client = clientService.getClientEntityByUsername(username);

        boolean isStarted = isWorkoutStarted(workoutId, username);
        if (!isStarted) {
            workout.getClientsStarted().add(client);
        }

        workoutRepository.save(workout);
    }

    public void completedWorkout(Long workoutId, String username) {
        WorkoutEntity workout = getWorkoutEntityById(workoutId);
        ClientEntity client = clientService.getClientEntityByUsername(username);

        boolean isCompleted = isWorkoutCompleted(workoutId, username);
        if (!isCompleted) {
            workout.getClientsCompleted().add(client);
        }

        workoutRepository.save(workout);
    }

    public boolean isWorkoutStarted(Long workoutId, String username) {
        WorkoutEntity workout = getWorkoutEntityById(workoutId);
        return workout.getClientsStarted().stream().anyMatch(clientEntity -> clientEntity.getUsername().equals(username));
    }

    public boolean isWorkoutCompleted(Long workoutId, String username) {
        WorkoutEntity workout = getWorkoutEntityById(workoutId);
        return workout.getClientsCompleted().stream().anyMatch(clientEntity -> clientEntity.getUsername().equals(username));
    }


}
