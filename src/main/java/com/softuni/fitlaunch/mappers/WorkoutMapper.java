package com.softuni.fitlaunch.mappers;

import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutMapper {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ExerciseMapper exerciseMapper;

    public WorkoutDTO mapToDTO(WorkoutEntity workoutEntity) {
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setId(workoutDTO.getId());
        workoutDTO.setAuthor(workoutEntity.getAuthor().getUsername());
        workoutDTO.setImgUrl(workoutEntity.getImgUrl());
        workoutDTO.setName(workoutEntity.getName());
        workoutDTO.setLevel(workoutEntity.getLevel());
        workoutDTO.setDescription(workoutEntity.getDescription());
        List<CommentCreationDTO> comments = workoutEntity.getComments().stream().map(commentMapper::mapAsCreationDTO).toList();
        workoutDTO.setComments(comments);
        workoutDTO.setLikes(workoutEntity.getLikes());
        List<WorkoutExerciseDTO> exercises = workoutEntity.getExercises().stream().map(exerciseMapper::mapAsDTO).toList();
        workoutDTO.setExercises(exercises);
        workoutDTO.setCompleted(workoutDTO.isCompleted());
        workoutDTO.setHasStarted(workoutEntity.isHasStarted());
        workoutDTO.setDateCompleted(workoutDTO.getDateCompleted());

        return workoutDTO;
    }
}
