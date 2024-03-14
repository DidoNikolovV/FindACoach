package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.view.CommentView;
import com.softuni.fitlaunch.model.entity.CommentEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.repository.CommentRepository;
import com.softuni.fitlaunch.repository.ProgramRepository;
import com.softuni.fitlaunch.repository.UserRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;


    private final UserRepository userRepository;;

    private final ProgramRepository programRepository;



    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ProgramRepository programRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.programRepository = programRepository;
    }

    public List<CommentView> getAllCommentsForWorkout(Long workoutId) {
        List<CommentEntity> comments = commentRepository.findAllByWorkoutId(workoutId);
        return comments.stream().map(commentEntity -> new CommentView(commentEntity.getId(), commentEntity.getAuthor().getUsername(), commentEntity.getMessage())).collect(Collectors.toList());
    }


    public CommentView addComment(CommentCreationDTO commentDTO) {
        UserEntity authorEntity = userRepository.findByUsername(commentDTO.getAuthorUsername()).get();

        ProgramEntity programEntity = programRepository.findById(commentDTO.getProgramId()).orElseThrow(() -> new ObjectNotFoundException("Program with id " + commentDTO.getProgramId() + " was not found"));

        CommentEntity comment = new CommentEntity();
        comment.setProgram(programEntity);
        comment.setAuthor(authorEntity);
        comment.setMessage(commentDTO.getMessage());
        commentRepository.save(comment);

        return new CommentView(comment.getId(), authorEntity.getUsername(), comment.getMessage());
    }

    public CommentView getComment(Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new ObjectNotFoundException("Comment with id " + commentId + " was not found"));
        return new CommentView(commentEntity.getId(), commentEntity.getAuthor().getUsername(), commentEntity.getMessage());
    }


    public void deleteCommentById(Long id) {
        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Comment with id " + id + " was not found"));
        commentRepository.delete(comment);
    }

}
