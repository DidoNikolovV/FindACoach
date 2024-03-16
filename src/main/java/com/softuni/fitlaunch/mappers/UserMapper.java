package com.softuni.fitlaunch.mappers;


import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<WorkoutDTO> workoutsLiked = userEntity.getWorkoutsLiked().stream().map(workoutMapper::mapToDTO).toList();
        userDTO.setWorkoutsLiked(workoutsLiked);
        BeanUtils.copyProperties(userEntity, userDTO);

        return userDTO;
    }

    public ClientEntity mapUserToClient(UserEntity user) {
        ClientEntity clientEntity = new ClientEntity();
        BeanUtils.copyProperties(user, clientEntity);
        clientEntity.setCoach(null);
        clientEntity.setWorkoutStarted(null);
        clientEntity.setProgramExercisesCompleted(new ArrayList<>());
        clientEntity.setScheduledWorkouts(new ArrayList<>());
        clientEntity.setCompletedPrograms(new ArrayList<>());
        clientEntity.setCompletedWorkouts(new ArrayList<>());

        return clientEntity;
    }

    public CoachEntity mapUserToCoach(UserEntity user) {
        CoachEntity coach = new CoachEntity();
        BeanUtils.copyProperties(user, coach);
        coach.setRating(1.0);
        coach.setDescription(null);
        coach.setCertificates(new ArrayList<>());
        coach.setPrograms(new ArrayList<>());
        coach.setClients(new ArrayList<>());
        coach.setScheduledWorkouts(new ArrayList<>());

        return coach;
    }

}
