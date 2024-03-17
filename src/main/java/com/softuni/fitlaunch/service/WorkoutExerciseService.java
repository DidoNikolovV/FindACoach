package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
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

    public List<WorkoutExerciseDTO> getAllByWorkoutId(Long workoutId) {
        return workoutExerciseRepository.findAllByWorkoutId(workoutId).stream().map(exerciseEntity -> modelMapper.map(exerciseEntity, WorkoutExerciseDTO.class)).toList();
    }

}
