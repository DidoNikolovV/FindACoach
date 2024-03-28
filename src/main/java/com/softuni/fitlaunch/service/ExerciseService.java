package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import com.softuni.fitlaunch.repository.ExerciseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final ModelMapper modelMapper;

    public ExerciseService(ExerciseRepository exerciseRepository, ModelMapper modelMapper) {
        this.exerciseRepository = exerciseRepository;
        this.modelMapper = modelMapper;
    }

    public List<WorkoutExerciseDTO> loadAllExercises() {
        return exerciseRepository.findAll().stream().map(exercise -> modelMapper.map(exercise, WorkoutExerciseDTO.class)).toList();
    }


    public ExerciseEntity getById(Long exerciseId) {
        return exerciseRepository.findById(exerciseId).orElse(null);
    }
}
