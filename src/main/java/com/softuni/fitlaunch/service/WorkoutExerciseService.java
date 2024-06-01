package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ModelMapper modelMapper;

    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository, ModelMapper modelMapper) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.modelMapper = modelMapper;
    }

    public List<WorkoutExerciseDTO> getAllExercises() {
        return workoutExerciseRepository.findAll()
                .stream()
                .map(exercise -> modelMapper.map(exercise, WorkoutExerciseDTO.class))
                .toList();
    }

    public List<WorkoutExerciseEntity> saveAll(List<WorkoutExerciseEntity> exercises) {
        return workoutExerciseRepository.saveAll(exercises);
    }

    public void completeExercise(Long exerciseId, Long workoutId) {
        WorkoutExerciseEntity exerciseToBeCompleted = workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId).orElseThrow(() -> new ResourceNotFoundException("Exercise does not exist"));
        exerciseToBeCompleted.setCompleted(true);
        workoutExerciseRepository.save(exerciseToBeCompleted);
    }
}
