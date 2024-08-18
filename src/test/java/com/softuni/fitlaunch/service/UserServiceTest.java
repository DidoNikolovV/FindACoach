package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.view.UserProfileView;
import com.softuni.fitlaunch.model.entity.UserActivationCodeEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserRoleEntity;
import com.softuni.fitlaunch.model.enums.UserRoleEnum;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.model.events.UserRegisteredEvent;
import com.softuni.fitlaunch.repository.RoleRepository;
import com.softuni.fitlaunch.repository.UserActivationCodeRepository;
import com.softuni.fitlaunch.repository.UserRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CoachService coachService;

    @Mock
    private ClientService clientService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private UserActivationCodeRepository userActivationCodeRepository;

    @Mock
    private FileUpload fileUpload;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegister_whenUserDetailsAreValid_thenUserIsRegisteredSuccessfully() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setConfirmPassword("password");
        dto.setTitle(UserTitleEnum.CLIENT.name());

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setRole(UserRoleEnum.CLIENT);

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setTitle(UserTitleEnum.CLIENT);

        when(userRepository.count()).thenReturn(0L);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(userRoleEntity));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(modelMapper.map(dto, UserEntity.class)).thenReturn(user);

        boolean result = userService.register(dto);

        assertTrue(result);
        verify(userRepository).save(any(UserEntity.class));
        verify(applicationEventPublisher).publishEvent(any(UserRegisteredEvent.class));
        verify(clientService, times(1)).registerClient(user);
    }

    @Test
    void testSaveUser_whenUserIsProvided_thenUserIsSaved() {
        UserEntity user = new UserEntity();
        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegister_whenUsernameIsTaken_thenRegistrationFails() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setConfirmPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new UserEntity()));

        boolean result = userService.register(dto);

        assertFalse(result);
    }

    @Test
    void testRegister_whenPasswordsDoNotMatch_thenRegistrationFails() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password");
        dto.setConfirmPassword("differentpassword");

        boolean result = userService.register(dto);

        assertFalse(result);
    }

    @Test
    void testChangeUserRole_whenRoleIsUpdatedToCoach_thenRoleIsChangedSuccessfully() {
        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setRoles(new ArrayList<>());

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        userService.changeUserRole("test", "COACH");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers_whenUsersExist_thenUsersAreReturned() {
        UserEntity user = new UserEntity();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(userDTO, users.get(0));
    }

    @Test
    void testGetUserByUsername_whenUserExists_thenUserDTOIsReturned() {
        UserEntity user = new UserEntity();
        UserDTO userDTO = new UserDTO();
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getUserByUsername(username);

        assertEquals(userDTO, result);
    }

    @Test
    void testGetUserByUsername_whenUserDoesNotExist_thenExceptionIsThrown() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    void testActivateUser_whenActivationCodeIsValid_thenUserIsActivated() {
        String activationCode = "validCode";
        UserEntity user = new UserEntity();
        UserActivationCodeEntity activationCodeEntity = new UserActivationCodeEntity();
        activationCodeEntity.setCreated(Instant.now().minus(1, ChronoUnit.DAYS));
        activationCodeEntity.setUser(user);

        when(userActivationCodeRepository.findByActivationCode(activationCode))
                .thenReturn(Optional.of(activationCodeEntity));
        when(userRepository.save(user)).thenReturn(user);

        boolean result = userService.activateUser(activationCode);

        assertTrue(result);
        assertTrue(user.isActivated());
        verify(userActivationCodeRepository).delete(activationCodeEntity);
    }

    @Test
    void testActivateUser_whenActivationCodeIsNotFound_thenExceptionIsThrown() {
        String activationCode = "invalidCode";

        when(userActivationCodeRepository.findByActivationCode(activationCode))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.activateUser(activationCode));
    }

    @Test
    @Disabled
    void testActivateUser_whenUserIsAlreadyActivated_thenActivationFails() {
        String activationCode = "validCode";
        UserEntity user = new UserEntity();
        user.setActivated(true);
        UserActivationCodeEntity activationCodeEntity = new UserActivationCodeEntity();
        activationCodeEntity.setCreated(Instant.now().minus(1, ChronoUnit.DAYS));
        activationCodeEntity.setUser(user);

        when(userActivationCodeRepository.findByActivationCode(activationCode))
                .thenReturn(Optional.of(activationCodeEntity));

        boolean result = userService.activateUser(activationCode);

        assertFalse(result);
    }

    @Test
    void testUploadProfilePicture_whenPictureIsUploaded_thenProfileIsUpdated() {
        String username = "testuser";
        MultipartFile profilePicture = mock(MultipartFile.class);
        String pictureUrl = "http://example.com/picture.jpg";

        when(fileUpload.uploadFile(profilePicture)).thenReturn(pictureUrl);

        UserEntity userEntity = new UserEntity();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserProfileView userProfileView = new UserProfileView();
        when(modelMapper.map(userEntity, UserProfileView.class)).thenReturn(userProfileView);

        UserProfileView result = userService.uploadProfilePicture(username, profilePicture);

        assertEquals(pictureUrl, userEntity.getImgUrl());
        assertEquals(userProfileView, result);
        verify(userRepository).save(userEntity);
    }

    @Test
    void testGetUserProfileByUsername_whenUserExists_thenUserProfileViewIsReturned() {
        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        UserProfileView userProfileView = new UserProfileView();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserProfileView.class)).thenReturn(userProfileView);

        UserProfileView result = userService.getUserProfileByUsername(username);

        assertEquals(userProfileView, result);
    }

    @Test
    void testGetUserEntityByEmail_whenUserWithThisEmailExists_thenReturnIt() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setId(1L);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        userService.getUserEntityByEmail("test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testChangeMembership_whenUserWantsToChangeHisMembership_thenChangeIt() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        UserDTO userDto = new UserDTO();
        userDto.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        userService.changeMembership(userDto, anyString());
    }
}
