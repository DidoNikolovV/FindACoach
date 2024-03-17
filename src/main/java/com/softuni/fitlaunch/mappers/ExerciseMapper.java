package com.softuni.fitlaunch.mappers;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ExerciseMapper {

    public WorkoutExerciseDTO mapAsDTO(WorkoutExerciseEntity exerciseEntity) {
        WorkoutExerciseDTO workoutExerciseDTO = new WorkoutExerciseDTO();
        BeanUtils.copyProperties(exerciseEntity, workoutExerciseDTO);
        return workoutExerciseDTO;
    }
}
