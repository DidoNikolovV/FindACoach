package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.view.UserProfileView;
import com.softuni.fitlaunch.model.entity.UserActivationCodeEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserRoleEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.UserRoleEnum;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.model.events.UserRegisteredEvent;
import com.softuni.fitlaunch.repository.RoleRepository;
import com.softuni.fitlaunch.repository.UserActivationCodeRepository;
import com.softuni.fitlaunch.repository.UserRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static com.softuni.fitlaunch.commons.ErrorMessages.ACTIVATION_CODE_NOT_FOUND;
import static com.softuni.fitlaunch.commons.ErrorMessages.USER_WITH_EMAIL_X_DOES_NOT_EXIST;
import static com.softuni.fitlaunch.commons.ErrorMessages.USER_WITH_USERNAME_X_DOES_NOT_EXIST;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final CoachService coachService;

    private final ClientService clientService;

    private final ProgramService programService;

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final UserActivationCodeRepository userActivationCodeRepository;

    private final FileUpload fileUpload;


    public UserService(UserRepository userRepository, CoachService coachService, RoleRepository roleRepository, ClientService clientService, ProgramService programService, ModelMapper modelMapper, ApplicationEventPublisher applicationEventPublisher, UserActivationCodeRepository userActivationCodeRepository, FileUpload fileUpload) {
        this.userRepository = userRepository;
        this.coachService = coachService;
        this.roleRepository = roleRepository;
        this.clientService = clientService;
        this.programService = programService;
        this.modelMapper = modelMapper;
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

        UserEntity user = modelMapper.map(userRegisterDTO, UserEntity.class);

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
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }


    public UserDTO getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException(String.format(USER_WITH_USERNAME_X_DOES_NOT_EXIST, username)));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserEntity getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException(String.format(USER_WITH_USERNAME_X_DOES_NOT_EXIST, username)));
    }

    public void like(Long workoutId, String username) {
        UserEntity user = getUserEntityByUsername(username);

        boolean hasLiked = user.getWorkoutsLiked().stream().anyMatch(workoutEntity -> Objects.equals(workoutEntity.getId(), workoutId));

        if(!hasLiked) {
            WorkoutEntity workout = programService.getWorkoutEntityById(workoutId);
            user.getWorkoutsLiked().add(workout);
            int oldLikes = workout.getLikes();
            workout.setLikes(oldLikes + 1);
        }

        userRepository.save(user);
    }

    public void dislike(Long workoutId, String username) {
        UserEntity user = getUserEntityByUsername(username);

        boolean hasLiked = user.getWorkoutsLiked().stream().anyMatch(workoutEntity -> Objects.equals(workoutEntity.getId(), workoutId));

        if(hasLiked) {
            WorkoutEntity workout = programService.getWorkoutEntityById(workoutId);
            user.getWorkoutsLiked().remove(workout);
            int oldLikes = workout.getLikes();
            workout.setLikes(oldLikes - 1);
        }

        userRepository.save(user);
    }

    public boolean isWorkoutLiked(Long workoutId, String username) {
        UserEntity user = getUserEntityByUsername(username);

        return user.getWorkoutsLiked().stream()
                .anyMatch(likedWorkout -> likedWorkout.getId().equals(workoutId));
    }

    public void changeUserRole(String username, String role) {
        UserEntity userEntity = getUserEntityByUsername(username);

        Long roleId = role.equals(UserRoleEnum.ADMIN.name()) ? 1L : 2L;

        UserRoleEntity newRole = new UserRoleEntity()
                .setId(roleId)
                .setRole(UserRoleEnum.valueOf(role));

        userEntity.getRoles().clear();
        userEntity.getRoles().add(newRole);
        userRepository.save(userEntity);
    }

    public boolean activateUser(String activationCode) {

        UserActivationCodeEntity activationCodeEntity = userActivationCodeRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ObjectNotFoundException(ACTIVATION_CODE_NOT_FOUND));
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
        UserEntity userEntity = userRepository.findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException(String.format(USER_WITH_USERNAME_X_DOES_NOT_EXIST, loggedUser.getUsername())));
        userRepository.save(userEntity);
    }

    public UserProfileView uploadProfilePicture(String username, MultipartFile profilePicture) {
        String picture = fileUpload.uploadFile(profilePicture);

        UserEntity userEntity = getUserEntityByUsername(username);
        userEntity.setImgUrl(picture);

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserProfileView.class);
    }

    public UserProfileView getUserProfileByUsername(String username) {
        UserEntity userEntity = getUserEntityByUsername(username);
        return modelMapper.map(userEntity, UserProfileView.class);
    }

    public UserEntity getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException(String.format(USER_WITH_EMAIL_X_DOES_NOT_EXIST, email)));
    }
}
