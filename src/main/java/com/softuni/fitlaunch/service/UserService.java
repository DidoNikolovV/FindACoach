package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.mappers.UserMapper;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.user.UserRoleDTO;
import com.softuni.fitlaunch.model.dto.view.UserProfileView;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.UserActivationCodeEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserRoleEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.model.events.UserRegisteredEvent;
import com.softuni.fitlaunch.repository.RoleRepository;
import com.softuni.fitlaunch.repository.UserActivationCodeRepository;
import com.softuni.fitlaunch.repository.UserRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final CoachService coachService;

    private final ClientService clientService;

    private final ProgramService programService;

    private final RoleRepository roleRepository;


    private final ModelMapper modelMapper;

    private final UserMapper userMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final UserActivationCodeRepository userActivationCodeRepository;

    private final FileUpload fileUpload;


    public UserService(UserRepository userRepository, CoachService coachService, RoleRepository roleRepository, ClientService clientService, ProgramService programService, ModelMapper modelMapper, UserMapper userMapper, ApplicationEventPublisher applicationEventPublisher, UserActivationCodeRepository userActivationCodeRepository, FileUpload fileUpload) {
        this.userRepository = userRepository;
        this.coachService = coachService;
        this.roleRepository = roleRepository;
        this.clientService = clientService;
        this.programService = programService;
        this.modelMapper = modelMapper;
        this.userMapper = userMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.userActivationCodeRepository = userActivationCodeRepository;
        this.fileUpload = fileUpload;
    }

    public boolean register(UserRegisterDTO userRegisterDTO) {
        if(passwordsDontMatch(userRegisterDTO.getPassword(), userRegisterDTO.getConfirmPassword()) || isUsernameTaken(userRegisterDTO.getUsername())) {
            return false;
        }

        boolean isFirst = userRepository.count() == 0;

        UserRoleEntity role = roleRepository.findById(isFirst ? 1L : UserTitleEnum.valueOf(userRegisterDTO.getTitle()).ordinal() + 1).orElse(null);

        UserEntity user = userMapper.mapToEntity(userRegisterDTO);

        user.getRoles().add(role);
        user.setTitle(UserTitleEnum.valueOf(userRegisterDTO.getTitle()));

        userRepository.save(user);

        if(user.getTitle().equals(UserTitleEnum.CLIENT)) {
            clientService.registerClient(user);
        }
        else if(user.getTitle().equals(UserTitleEnum.COACH)) {
            coachService.registerCoach(user);
        }

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(
                "UserService", userRegisterDTO.getEmail(), userRegisterDTO.getUsername()
        ));

        return true;
    }

    private boolean passwordsDontMatch(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::mapToDTO).toList();
    }


    public UserDTO getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User with " + username + " doesn't exist"));
        return userMapper.mapToDTO(userEntity);
    }

    public UserEntity getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User with " + username + " doesn't exist"));
    }

    public void like(UserDTO loggedUser, Long workoutId) {
        UserEntity userEntity = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        WorkoutEntity weekWorkout = programService.getWorkoutEntityById(workoutId);
        int oldLikes = weekWorkout.getLikes();

        boolean hasNotLiked = true;

        for (WorkoutEntity likedWorkout : userEntity.getWorkoutsLiked()) {
            if (likedWorkout.getId().equals(workoutId)) {
                hasNotLiked = false;
                break;
            }
        }

        if(hasNotLiked) {
            userEntity.getWorkoutsLiked().add(weekWorkout);
            weekWorkout.setLikes(oldLikes + 1);
        }

        userRepository.save(userEntity);
    }

    public void dislike(UserDTO loggedUser, Long workoutId) {
        UserEntity userEntity = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        WorkoutEntity weekWorkout = programService.getWorkoutEntityById(workoutId);
        userEntity.getWorkoutsLiked().remove(weekWorkout);

        programService.removeLike(weekWorkout);
        userRepository.save(userEntity);
    }

    public boolean isWorkoutLiked(UserDTO loggedUser, Long workoutId) {
        UserEntity user = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User with " + loggedUser.getUsername() + " doesn't exist"));

        return user.getWorkoutsLiked().stream()
                .anyMatch(likedWorkout -> likedWorkout.getId().equals(workoutId));
    }

    public void changeUserRole(String username, UserRoleEntity role) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        userEntity.getRoles().clear();
        userEntity.getRoles().add(role);
        userRepository.save(userEntity);
    }

    public UserRoleDTO getUserRole(UserDTO userDTO) {
        UserEntity userEntity = userRepository.findByUsername(userDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        UserRoleEntity userRoleEntity = userEntity.getRoles().get(0);

        return new UserRoleDTO(
                userRoleEntity.getId(),
                userRoleEntity.getRole()
        );
    }

    public boolean activateUser(String activationCode) {

        UserActivationCodeEntity activationCodeEntity = userActivationCodeRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ObjectNotFoundException("Activation code not found"));
        UserEntity user = activationCodeEntity.getUser();

        if (user.isActivated() && isActivationCodeExpired(activationCodeEntity.getCreated())) {
            return false;
        }

        user.setActivated(true);
        user.setActivationCode(null);
        user.setActivationCodeExpiration(null);

        userRepository.save(user);
        userActivationCodeRepository.delete(activationCodeEntity);

        return true;
    }

    private boolean isActivationCodeExpired(Instant expirationDateTime) {
        return expirationDateTime != null && expirationDateTime.isBefore(expirationDateTime);
    }


    public void changeMembership(UserDTO loggedUser, String membership) {
        UserEntity userEntity = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User with username " + loggedUser.getUsername() + " was not found"));
        userEntity.setMembership(membership);
        userRepository.save(userEntity);
    }

    public UserProfileView uploadProfilePicture(String username, MultipartFile profilePicture) throws IOException {
        String picture = fileUpload.uploadFile(profilePicture);

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        userEntity.setImgUrl(picture);

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserProfileView.class);
    }

    public UserProfileView getUserProfileByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User with " + username + " doesn't exist"));
        return modelMapper.map(userEntity, UserProfileView.class);
    }
}
