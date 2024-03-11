package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.ExerciseDTO;
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

    public List<ExerciseDTO> getAllExercises() {
        return exerciseRepository.findAll()
                .stream()
                .map(exercise -> modelMapper.map(exercise, ExerciseDTO.class))
                .toList();
    }

    public List<ExerciseDTO> getAllByWorkoutId(Long workoutId) {
        return exerciseRepository.findAllByWorkoutId(workoutId).stream().map(exerciseEntity -> modelMapper.map(exerciseEntity, ExerciseDTO.class)).toList();
    }

//    public void completeExercise(Long workoutId, Long exerciseId) {
//        WorkoutDTO workout = workoutService.getWorkoutById(workoutId);
//        for (ExerciseDTO exercise : workout.getExercises()) {
//            if(Objects.equals(exercise.getId(), exerciseId)) {
//                exercise.setCompleted(true);
//                break;
//            }
//        }
//    }
}
