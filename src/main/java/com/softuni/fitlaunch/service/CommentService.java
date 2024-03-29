package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.view.CommentView;
import com.softuni.fitlaunch.model.entity.CommentEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.UserRoleEnum;
import com.softuni.fitlaunch.repository.CommentRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserService userService;

    private final WorkoutService workoutService;

    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, UserService userService, WorkoutService workoutService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.workoutService = workoutService;
        this.modelMapper = modelMapper;
    }

    public List<CommentView> getAllCommentsForWorkout(Long workoutId) {
        List<CommentEntity> comments = commentRepository.findAllByWorkoutId(workoutId);
        return comments.stream().map(commentEntity -> new CommentView(commentEntity.getId(), commentEntity.getAuthor().getUsername(), commentEntity.getMessage())).collect(Collectors.toList());
    }


    public CommentView addComment(CommentCreationDTO commentDTO, String username, Long workoutId) {
        UserEntity author = userService.getUserEntityByUsername(username);
        WorkoutEntity workout = workoutService.getWorkoutEntityById(workoutId);
        CommentEntity comment = modelMapper.map(commentDTO, CommentEntity.class);
        comment.setAuthor(author);
        comment.setWorkout(workout);
        comment = commentRepository.save(comment);

        return new CommentView(comment.getId(), author.getUsername(), comment.getMessage());
    }


    public CommentView getComment(Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new ObjectNotFoundException("Comment with id " + commentId + " was not found"));
        return new CommentView(commentEntity.getId(), commentEntity.getAuthor().getUsername(), commentEntity.getMessage());
    }


    public void deleteCommentById(Long id, String username) {
        UserDTO user = userService.getUserByUsername(username);

        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Comment with id " + id + " was not found"));

        if (user.getRoles().stream().anyMatch(r -> r.getRole().equals(UserRoleEnum.ADMIN)) || user.getUsername().equals(comment.getAuthor().getUsername())) {
            commentRepository.delete(comment);
        }

    }

}
