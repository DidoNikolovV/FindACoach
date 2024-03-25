package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
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

    private final ModelMapper modelMapper;


    public WorkoutService(WorkoutRepository workoutRepository, ModelMapper modelMapper) {
        this.workoutRepository = workoutRepository;
        this.modelMapper = modelMapper;
    }

    public WorkoutDTO createWorkout(WorkoutEntity workout) {
        WorkoutEntity newWorkout = workoutRepository.save(workout);
        return modelMapper.map(newWorkout, WorkoutDTO.class);
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

    public List<WorkoutDTO> loadAllByProgramId(Long programId) {
        return workoutRepository
                .findAllByProgramId(programId)
                .stream()
                .map(workout -> modelMapper.map(workout, WorkoutDTO.class))
                .toList();
    }

    public List<LevelEnum> getAllLevels() {
        return Arrays.stream(LevelEnum.values()).collect(Collectors.toList());
    }
}
