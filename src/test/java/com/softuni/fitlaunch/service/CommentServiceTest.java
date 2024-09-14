package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRoleDTO;
import com.softuni.fitlaunch.model.dto.view.CommentView;
import com.softuni.fitlaunch.model.entity.CommentEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.UserRoleEnum;
import com.softuni.fitlaunch.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private WorkoutService workoutService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCommentsForWorkout_whenWorkoutHasComments_thenReturnAll() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setWorkout(workout);
        comment.setMessage("Test comment");
        comment.setId(1L);

        when(commentRepository.findAllByWorkoutId(1L)).thenReturn(List.of(comment));

        List<CommentView> allCommentsForWorkout = underTest.getAllCommentsForWorkout(1L);

        assertThat(allCommentsForWorkout).isNotEmpty();

        verify(commentRepository, times(1)).findAllByWorkoutId(1L);
    }

    @Test
    void testGetComment_whenCommentInDatabase_thenReturnIt() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setWorkout(workout);
        comment.setMessage("Test comment");
        comment.setId(1L);


        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentView result = underTest.getComment(1L);

        verify(commentRepository, times(1)).findById(1L);

        assertNotNull(result);
    }


    @Test
    void testDeleteCommentById_whenUserIsAuthorOfTheComment_thenDeleteIt() {
        UserDTO userDto = new UserDTO();
        userDto.setUsername("test");

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);

        UserRoleDTO userRoleDto = new UserRoleDTO();
        userRoleDto.setRole(UserRoleEnum.COACH);
        userDto.setRoles(List.of(userRoleDto));

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setMessage("Test comment");
        comment.setId(1L);
        comment.setWorkout(workout);

        workout.getComments().add(comment);

        when(userService.getUserByUsername("test")).thenReturn(userDto);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        underTest.deleteCommentById(1L, "test");

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testAddComment_whenUserAddComment_thenSaveIt() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");

        CommentCreationDTO commentCreationDTO = new CommentCreationDTO();

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setMessage("Test comment");
        comment.setId(1L);
        comment.setWorkout(workout);

        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(workoutService.getWorkoutEntityById(1L)).thenReturn(workout);
        when(modelMapper.map(commentCreationDTO, CommentEntity.class)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);

        underTest.addComment(commentCreationDTO, "test", 1L);

        verify(commentRepository, times(1)).save(comment);
    }
}