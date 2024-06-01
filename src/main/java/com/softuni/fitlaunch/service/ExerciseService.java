package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import com.softuni.fitlaunch.repository.ExerciseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<WorkoutExerciseDTO> loadAllExercises(Pageable pageable) {
        return exerciseRepository.findAll(pageable).map(exercise -> modelMapper.map(exercise, WorkoutExerciseDTO.class));
    }

    public ExerciseEntity getByName(String name) {
        return exerciseRepository.findByName(name).orElse(null);
    }

}
