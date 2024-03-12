package com.softuni.fitlaunch.mappers;


import com.fasterxml.jackson.databind.util.BeanUtil;
import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private WorkoutMapper workoutMapper;

    public UserEntity mapToEntity(UserRegisterDTO userRegisterDTO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userRegisterDTO, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userEntity.setImgUrl("/images/profile-avatar.jpg");
        userEntity.setMembership("Yearly");

        return userEntity;
    }
    
    public UserDTO mapToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        List<CommentCreationDTO> comments = userEntity.getComments().stream().map(commentMapper::mapAsCreationDTO).toList();
        userDTO.setComments(comments);
        userDTO.setWorkoutStarted(userEntity.getWorkoutStarted().getName());
        List<WorkoutDTO> workoutsLiked = userEntity.getWorkoutsLiked().stream().map(workoutMapper::mapToDTO).toList();
        userDTO.setWorkoutsLiked(workoutsLiked);
        BeanUtils.copyProperties(userEntity, userDTO);

        return userDTO;
    }

}
