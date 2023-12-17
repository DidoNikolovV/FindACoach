package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.view.CommentView;
import com.softuni.fitlaunch.model.entity.*;
import com.softuni.fitlaunch.repository.*;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final WorkoutRepository workoutRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final ProgramRepository programRepository;

    private final ProgramWeekRepository programWeekRepository;
    private final ProgramWeekWorkoutRepository programWeekWorkoutRepository;

    public CommentService(CommentRepository commentRepository, WorkoutRepository workoutRepository, UserRepository userRepository, ModelMapper modelMapper, ProgramRepository programRepository, ProgramWeekRepository programWeekRepository, ProgramWeekWorkoutRepository programWeekWorkoutRepository) {
        this.commentRepository = commentRepository;
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.programRepository = programRepository;
        this.programWeekRepository = programWeekRepository;
        this.programWeekWorkoutRepository = programWeekWorkoutRepository;
    }

    public List<CommentView> getAllCommentsForWorkout(Long workoutId) {
        WorkoutEntity workout = workoutRepository.findById(workoutId).orElseThrow(() -> new ObjectNotFoundException("Workout not found"));

        List<CommentEntity> comments = commentRepository.findAllByWorkout(workout).get();

        return comments.stream().map(commentEntity -> new CommentView(commentEntity.getId(), commentEntity.getAuthor().getUsername(), commentEntity.getContent())).collect(Collectors.toList());

    }

    public List<CommentView> getAllCommentsForWorkout(Long programId, Long weekId, Long workoutId) {
//        WorkoutEntity workout = workoutRepository.findById(workoutId).orElseThrow(() -> new ObjectNotFoundException("Workout not found"));

        List<CommentEntity> comments = commentRepository.findByProgramIdAndWeekIdAndWorkoutId(programId, weekId, workoutId).orElseThrow(() -> new ObjectNotFoundException("Comments not found"));
        List<CommentView> commentsDTO = comments.stream().map(commentEntity -> new CommentView(commentEntity.getId(), commentEntity.getAuthor().getUsername(), commentEntity.getContent())).collect(Collectors.toList());

        return commentsDTO;
    }


    public CommentView addComment(CommentCreationDTO commentDTO) {
        UserEntity authorEntity = userRepository.findByUsername(commentDTO.getAuthorName()).get();

        ProgramEntity programEntity = programRepository.findById(commentDTO.getProgramId()).orElseThrow(() -> new ObjectNotFoundException("Program with id " + commentDTO.getProgramId() + " was not found"));
        ProgramWeekEntity programWeekEntity = programWeekRepository.findById(commentDTO.getWeekId()).orElseThrow(() -> new ObjectNotFoundException("Week with id " + commentDTO.getWeekId() + " was not found"));
        ProgramWeekWorkoutEntity programWeekWorkoutEntity = programWeekWorkoutRepository.findById(commentDTO.getWorkoutId()).orElseThrow(() -> new ObjectNotFoundException("Workout with id " + commentDTO.getWorkoutId() + " was not found"));


        CommentEntity comment = new CommentEntity();
        comment.setProgram(programEntity);
        comment.setWeek(programWeekEntity);
        comment.setWorkout(programWeekWorkoutEntity);
        comment.setAuthor(authorEntity);
        comment.setContent(commentDTO.getMessage());
        commentRepository.save(comment);

        return new CommentView(comment.getId(), authorEntity.getUsername(), comment.getContent());

    }

    public CommentView getComment(Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new ObjectNotFoundException("Comment with id " + commentId + " was not found"));
        return new CommentView(commentEntity.getId(), commentEntity.getAuthor().getUsername(), commentEntity.getContent());
    }


    @Transactional
    public CommentView deleteCommentById(Long id) {
        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Comment with id " + id + " was not found"));
        CommentView commentView = new CommentView(comment.getId(),comment.getAuthor().getUsername(), comment.getContent());
        commentRepository.delete(comment);
        return commentView;
    }

}
