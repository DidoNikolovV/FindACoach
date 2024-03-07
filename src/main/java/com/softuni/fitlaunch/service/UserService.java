package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.user.UserRoleDTO;
import com.softuni.fitlaunch.model.dto.view.UserProfileView;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekWorkoutEntity;
import com.softuni.fitlaunch.model.entity.ProgramWorkoutExerciseEntity;
import com.softuni.fitlaunch.model.entity.UserActivationCodeEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserRoleEntity;
import com.softuni.fitlaunch.model.enums.UserRoleEnum;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.model.events.UserRegisteredEvent;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.repository.CoachRepository;
import com.softuni.fitlaunch.repository.ExerciseRepository;
import com.softuni.fitlaunch.repository.ProgramRepository;
import com.softuni.fitlaunch.repository.ProgramWeekRepository;
import com.softuni.fitlaunch.repository.ProgramWeekWorkoutRepository;
import com.softuni.fitlaunch.repository.RoleRepository;
import com.softuni.fitlaunch.repository.UserActivationCodeRepository;
import com.softuni.fitlaunch.repository.UserRepository;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
import com.softuni.fitlaunch.repository.WorkoutRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final RoleRepository roleRepository;
    private final WorkoutRepository workoutRepository;

    private final ProgramWeekWorkoutRepository programWeekWorkoutRepository;
    private final ProgramWeekRepository programWeekRepository;

    private final WorkoutExerciseRepository workoutExerciseRepository;

    private final ExerciseRepository exerciseRepository;
    
    private final CoachRepository coachRepository;
    private final ClientRepository clientRepository;

    private final ProgramRepository programRepository;

    private final ModelMapper modelMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final UserActivationCodeRepository userActivationCodeRepository;

    private final FileUpload fileUpload;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, WorkoutRepository workoutRepository, ProgramWeekWorkoutRepository programWeekWorkoutRepository, ProgramWeekRepository programWeekRepository, WorkoutExerciseRepository workoutExerciseRepository, ExerciseRepository exerciseRepository, CoachRepository coachRepository, ClientRepository clientRepository, ProgramRepository programRepository, ModelMapper modelMapper, ApplicationEventPublisher applicationEventPublisher, UserActivationCodeRepository userActivationCodeRepository, FileUpload fileUpload) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.workoutRepository = workoutRepository;
        this.programWeekWorkoutRepository = programWeekWorkoutRepository;
        this.programWeekRepository = programWeekRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.exerciseRepository = exerciseRepository;
        this.coachRepository = coachRepository;
        this.clientRepository = clientRepository;
        this.programRepository = programRepository;
        this.modelMapper = modelMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.userActivationCodeRepository = userActivationCodeRepository;
        this.fileUpload = fileUpload;
    }

    public boolean register(UserRegisterDTO userRegisterDTO) {

        boolean isFirst = userRepository.count() == 0;

        if(!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            return false;
        }

        Optional<UserEntity> dbUser = userRepository.findByUsername(userRegisterDTO.getUsername());

        if(dbUser.isPresent()) {
            return false;
        }

        UserRoleEntity role = roleRepository.findById(isFirst ? 1L : UserTitleEnum.valueOf(userRegisterDTO.getTitle()).ordinal()).orElse(null);


        UserEntity user = new UserEntity();
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRoles(List.of(role));
        user.setImgUrl("/images/profile-avatar.jpg");


        if(userRegisterDTO.getTitle().equals((UserTitleEnum.CLIENT.name()))) {
            user.setMembership("Free");
        } else {
            user.setMembership("Yearly");
        }
        user.setTitle(UserTitleEnum.valueOf(userRegisterDTO.getTitle()));
        
        if(isClient(user)) {
            ClientEntity clientEntity = modelMapper.map(user, ClientEntity.class);
            clientEntity.setCoach(null);
            clientRepository.save(clientEntity);
        } else {
            CoachEntity coachEntity = modelMapper.map(user, CoachEntity.class);
            coachEntity.setRating(1.0);
            coachEntity.setPrograms(programRepository.findAll());
            coachEntity.setClients(new ArrayList<>());
            coachEntity.setRole(UserRoleEnum.ADMIN);
            coachRepository.save(coachEntity);
        }
        userRepository.save(user);

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(
                "UserService", userRegisterDTO.getEmail(), userRegisterDTO.getUsername()
        ));

        return true;
    }

    private static boolean isClient(UserEntity user) {
        return user.getTitle().equals(UserTitleEnum.CLIENT);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userEntity -> modelMapper.map(userEntity, UserDTO.class)).toList();
    }

    public UserDTO getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        return modelMapper.map(userEntity, UserDTO.class);
    }

    public UserDTO getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User with " + username + " doesn't exist"));
        return modelMapper.map(userEntity, UserDTO.class);

    }

    public void startProgramWorkout(String username, ProgramWeekWorkoutDTO programWeekWorkoutDTO) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User with " + username + " doesn't exist"));
        ProgramWeekWorkoutEntity programWeekWorkoutEntity = programWeekWorkoutRepository.findById(programWeekWorkoutDTO.getId()).orElseThrow(() -> new ObjectNotFoundException("Workout not found"));
        user.getWorkoutsStarted().add(programWeekWorkoutEntity);
        userRepository.save(user);
    }

    public boolean isWorkoutStarted(String username, ProgramWeekWorkoutDTO programWeekWorkoutDTO) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User with " + username + " doesn't exist"));

        for (ProgramWeekWorkoutEntity weekWorkoutEntity : user.getWorkoutsStarted()) {
            if(weekWorkoutEntity.getId().equals(programWeekWorkoutDTO.getId())) {
                return true;
            }
        }

        return false;
    }

    public void completeProgramWorkout(String username, ProgramWeekWorkoutDTO programWeekWorkoutDTO) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with " + username + " was not found"));

        ProgramWeekWorkoutEntity programWeekWorkoutEntity = programWeekWorkoutRepository.findById(programWeekWorkoutDTO.getId()).orElseThrow(() -> new ObjectNotFoundException("Workout not found"));
        clientEntity.getCompletedWorkouts().add(programWeekWorkoutEntity);
        clientRepository.save(clientEntity);
    }

    public boolean isWorkoutCompleted(String username, ProgramWeekWorkoutDTO programWeekWorkoutDTO) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with " + username + " was not found"));
        for (ProgramWeekWorkoutEntity weekWorkoutEntity : clientEntity.getCompletedWorkouts()) {
            if(weekWorkoutEntity.getId().equals(programWeekWorkoutDTO.getId())) {
                return true;
            }
        }

        return false;
    }

    public void like(UserDTO loggedUser, Long workoutId) {
        UserEntity userEntity = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        ProgramWeekWorkoutEntity workoutEntity = programWeekWorkoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
        Long oldLikes = workoutEntity.getLikes();
        userEntity.getWorkoutsLiked().add(workoutEntity);
        workoutEntity.setLikes(oldLikes + 1);


        programWeekWorkoutRepository.save(workoutEntity);
        userRepository.save(userEntity);
    }

    public void dislike(UserDTO loggedUser, Long workoutId) {
        UserEntity userEntity = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        ProgramWeekWorkoutEntity workoutEntity = programWeekWorkoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));

        Long oldLikes = workoutEntity.getLikes();
        userEntity.getWorkoutsLiked().remove(workoutEntity);
        if(oldLikes - 1 < 0) {
            workoutEntity.setLikes(0L);
        } else {
            workoutEntity.setLikes(oldLikes - 1);
        }

        programWeekWorkoutRepository.save(workoutEntity);
        userRepository.save(userEntity);
    }

    public boolean isWorkoutLiked(UserDTO loggedUser, ProgramWeekWorkoutDTO programWeekWorkoutDTO) {
        UserEntity user = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User with " + loggedUser.getUsername() + " doesn't exist"));
        for (ProgramWeekWorkoutEntity likedWorkout : user.getWorkoutsLiked()) {
            if(likedWorkout.getId().equals(programWeekWorkoutDTO.getId())) {
                return true;
            }
        }

        return false;
    }

    public void changeUserRole(String username, UserRoleEntity role) {
        Optional<UserEntity> optUser = userRepository.findByUsername(username);

        if(optUser.isPresent()) {
            UserEntity user = optUser.get();
            if(user.getRoles() == null) {
                user.setRoles(new ArrayList<>());
            }


            user.getRoles().clear();
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }


    @Transactional
    public List<WorkoutDTO> getCompletedWorkouts(String username) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<WorkoutDTO> completedWorkouts = clientEntity.getCompletedWorkouts().stream().map(workoutEntity -> modelMapper.map(workoutEntity, WorkoutDTO.class)).toList();

        return completedWorkouts;
    }


    public UserRoleDTO getUserRole(UserDTO userDTO) {
        Optional<UserEntity> optUser =
                userRepository.findByUsername(userDTO.getEmail());

        if(optUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        UserEntity user = optUser.get();

        UserRoleEntity userRoleEntity = user.getRoles().get(0);

        return new UserRoleDTO(
                userRoleEntity.getId(),
                userRoleEntity.getRole()
        );
    }

    public boolean activateUser(String activationCode) {

        UserActivationCodeEntity activationCodeEntity = userActivationCodeRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ObjectNotFoundException("Activation code not found"));
        UserEntity user = activationCodeEntity.getUser();
        System.out.println();

        if (!user.isActivated() && !isActivationCodeExpired(activationCodeEntity.getCreated())) {
            user.setActivated(true);
            user.setActivationCode(null);
            user.setActivationCodeExpiration(null);

            userRepository.save(user);
            userActivationCodeRepository.delete(activationCodeEntity);

            return true;
        }

        return false;
    }

    private boolean isActivationCodeExpired(Instant expirationDateTime) {
        return expirationDateTime != null && expirationDateTime.isBefore(expirationDateTime);
    }


    public void changeMembership(UserDTO loggedUser, String membership) {
        UserEntity userEntity = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User with username " + loggedUser.getUsername() + " was not found"));
        userEntity.setMembership(membership);
        userRepository.save(userEntity);
    }



    public void completeProgramWorkoutExercise(UserDTO loggedUser, Long weekId, Long workoutId, Long exerciseId) {
        UserEntity userEntity = userRepository.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        ProgramWeekWorkoutEntity programWeekWorkoutEntity = programWeekWorkoutRepository.findById(workoutId).orElseThrow(() -> new ObjectNotFoundException("Workout not found"));

        for (ProgramWorkoutExerciseEntity exercise : programWeekWorkoutEntity.getExercises()) {
            if(exercise.getId().equals(exerciseId)) {
                userEntity.getProgramExercisesCompleted().add(exercise);
                userRepository.save(userEntity);
                return;
            }
        }
    }



    public UserProfileView uploadProfilePicture(String username, MultipartFile profilePicture) throws IOException {
        String picture = fileUpload.uploadFile(profilePicture);

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        userEntity.setImgUrl(picture);

        userRepository.save(userEntity);

        UserProfileView profileView = modelMapper.map(userEntity, UserProfileView.class);
        return profileView;

    }

    public UserProfileView getUserProfileByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User with " + username + " doesn't exist"));
        return modelMapper.map(userEntity, UserProfileView.class);
    }

    public boolean hasCompletedWorkout(ClientDTO clientDTO, ProgramWeekWorkoutDTO programWeekWorkoutDTO) {
        ClientEntity clientEntity = clientRepository.findByUsername(clientDTO.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        for (ProgramWeekWorkoutEntity programWeekWorkoutEntity : clientEntity.getCompletedWorkouts()) {
            if(programWeekWorkoutEntity.getId().equals(programWeekWorkoutDTO.getId())) {
                return true;
            }
        }

        return false;
    }


}
