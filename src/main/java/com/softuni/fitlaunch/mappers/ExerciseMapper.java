package com.softuni.fitlaunch.mappers;


import com.softuni.fitlaunch.model.dto.ExerciseDTO;
import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ExerciseMapper {

    public ExerciseDTO mapAsDTO(ExerciseEntity exerciseEntity) {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        BeanUtils.copyProperties(exerciseEntity, exerciseDTO);
        return exerciseDTO;
    }
}
