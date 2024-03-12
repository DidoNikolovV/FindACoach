package com.softuni.fitlaunch.mappers;


import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.entity.CommentEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {

    public CommentCreationDTO mapAsCreationDTO(CommentEntity commentEntity) {
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO();
        commentCreationDTO.setAuthorUsername(commentEntity.getAuthor().getUsername());
        commentCreationDTO.setProgramId(commentEntity.getProgram().getId());
        commentCreationDTO.setWeekId(commentEntity.getWeek().getId());
        commentCreationDTO.setWorkoutId(commentEntity.getWeek().getId());
        commentCreationDTO.setMessage(commentEntity.getMessage());

        return commentCreationDTO;
    }
}
