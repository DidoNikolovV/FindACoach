package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.program.ProgramCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProgress;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.repository.ProgramRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;
    @Mock
    private CoachService coachService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private WeekService weekService;
    @Mock
    private WorkoutService workoutService;
    @Mock
    private UserService userService;
    @Mock
    private ClientService clientService;
    @Mock
    private UserProgressService userProgressService;

    @InjectMocks
    private ProgramService underTest;

    public ProgramServiceTest() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testLoadAllPrograms_whenLoggedUserIsClientThenLoadAllProgramsFromHisCoach_thenLoadThem() {
        UserDTO user = new UserDTO();
        user.setUsername("test");
        user.setTitle(UserTitleEnum.CLIENT);

        CoachDTO coachDto = new CoachDTO();
        coachDto.setId(1L);
        coachDto.setUsername("test coach");

        ClientDTO clientDto = new ClientDTO();
        clientDto.setUsername("test");
        clientDto.setCoach(coachDto);

        List<ProgramEntity> programs = new ArrayList<>();
        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setId(1L);
        programEntity.setName("Push");
        programs.add(programEntity);

        ProgramDTO programDTO = new ProgramDTO();
        programDTO.setId(programEntity.getId());
        programDTO.setName(programEntity.getName());

        when(userService.getUserByUsername("test")).thenReturn(user);
        when(clientService.getClientByUsername(user.getUsername())).thenReturn(clientDto);
        when(coachService.getCoachById(1L)).thenReturn(coachDto);
        when(programRepository.findAllByCoachId(1L)).thenReturn(Optional.of(programs));
        when(modelMapper.map(programEntity, ProgramDTO.class)).thenReturn(programDTO);

        underTest.loadAllPrograms("test");

        verify(userService, times(1)).getUserByUsername("test");
        verify(clientService, times(1)).getClientByUsername("test");
        verify(coachService, times(1)).getCoachById(1L);
    }

    @Test
    void testGetAllWeeksByProgramId_whenProgramExists_thenReturnAllItsWeeks() {
        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setId(1L);
        programEntity.setName("Push");
        programEntity.setWeeks(new ArrayList<>());

        when(programRepository.findById(1L)).thenReturn(Optional.of(programEntity));

        underTest.getAllWeeksByProgramId(1L);

        verify(programRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_whenProgramExists_thenReturnProgramDto() {
        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setId(1L);
        programEntity.setName("Push");
        programEntity.setWeeks(new ArrayList<>());

        ProgramDTO programDTO = new ProgramDTO();
        programDTO.setId(programEntity.getId());
        programDTO.setName(programEntity.getName());
        programDTO.setWeeks(new ArrayList<>());

        when(programRepository.findById(1L)).thenReturn(Optional.of(programEntity));
        when(modelMapper.map(programEntity, ProgramDTO.class)).thenReturn(programDTO);
        underTest.getById(1L);

        verify(programRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProgramById_whenProgramExists_thenReturnIt() {
        ProgramEntity programEntity = new ProgramEntity();

        ProgramWeekEntity programWeekEntity = new ProgramWeekEntity();
        programWeekEntity.setNumber(1);
        programWeekEntity.setId(1L);
        programWeekEntity.setProgram(programEntity);

        ProgramWeekDTO programWeekDto = new ProgramWeekDTO();
        programWeekDto.setNumber(1);
        programWeekDto.setCompleted(false);
        programWeekDto.setDays(new ArrayList<>());

        programEntity.setId(1L);
        programEntity.setName("Push");
        programEntity.setWeeks(List.of(programWeekEntity));

        ProgramDTO programDTO = new ProgramDTO();
        programDTO.setId(programEntity.getId());
        programDTO.setName(programEntity.getName());
        programDTO.setWeeks(new ArrayList<>());

        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setId(1L);

        UserProgress userProgress = new UserProgress();
        userProgress.setProgram(programEntity);
        userProgress.setUser(user);
        userProgress.setWeek(programWeekEntity);
        List<UserProgress> userProgressList = new ArrayList<>();
        userProgressList.add(userProgress);

        when(programRepository.findById(1L)).thenReturn(Optional.of(programEntity));
//        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(userProgressService.getUserProgressForProgram("test", 1L)).thenReturn(userProgressList);
        when(modelMapper.map(programEntity, ProgramDTO.class)).thenReturn(programDTO);
        when(modelMapper.map(programWeekEntity, ProgramWeekDTO.class)).thenReturn(programWeekDto);

        underTest.getProgramById(1L, "test");

        verify(programRepository, times(1)).findById(1L);
//        verify(userService, times(1)).getUserEntityByUsername("test");
        verify(userProgressService, times(1)).getUserProgressForProgram("test", 1L);
    }

    @Test
    @Disabled
    void testCreateProgram_whenCoachTriesToCreateNewProgram_thenCreateAndPersistInDatabase() {
        CoachEntity coach = new CoachEntity();
        coach.setId(1L);
        coach.setUsername("test");

        ProgramCreationDTO programCreationDTO = new ProgramCreationDTO();
        programCreationDTO.setName("Full Body");
        programCreationDTO.setWeeks(1L);
        programCreationDTO.setDescription("description");

        ProgramEntity program = new ProgramEntity();

        List<ProgramWeekEntity> weeks = new ArrayList<>();
        ProgramWeekEntity week = new ProgramWeekEntity();
        week.setId(1L);
        week.setNumber(1);
        week.setDays(new ArrayList<>());
        week.setProgram(program);

        program.setName(programCreationDTO.getName());
        program.setCoach(coach);
        program.setWeeks(weeks);

        when(coachService.getCoachEntityByUsername("test")).thenReturn(coach);
        when(modelMapper.map(programCreationDTO, ProgramEntity.class)).thenReturn(program);
        when(programRepository.save(program)).thenReturn(program);

        underTest.createProgram(programCreationDTO, "test");

        verify(userProgressService, times(1)).saveUserProgress(program, "test", weeks);
    }

    @Test
    void testGetWeekById_whenWeekExists_thenReturnIt() {
        ProgramEntity program = new ProgramEntity();
        program.setId(1L);

        ProgramWeekEntity week = new ProgramWeekEntity();
        week.setNumber(1);
        week.setId(1L);
        week.setProgram(program);

        ProgramWeekDTO programWeekDto = new ProgramWeekDTO();
        programWeekDto.setNumber(1);
        programWeekDto.setCompleted(false);
        programWeekDto.setDays(new ArrayList<>());

        WorkoutEntity workout = new WorkoutEntity();
        workout.setName("Full Body");
        workout.setId(1L);

        WorkoutDTO workoutDto = new WorkoutDTO();
        workoutDto.setId(1L);
        workoutDto.setName("Full Body");

        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setId(1L);

        UserProgress userProgress = new UserProgress();
        userProgress.setUser(user);
        userProgress.setProgram(program);
        userProgress.setWeek(week);

        List<UserProgress> userProgressList = new ArrayList<>();
        userProgressList.add(userProgress);

        DayWorkoutsEntity day = new DayWorkoutsEntity();
        day.setId(1L);
        day.setWeek(week);
        day.setWorkout(workout);
        day.setName("test day");
        day.setWorkout(workout);

        userProgress.setWorkout(day);

        List<DayWorkoutsEntity> weekDays = List.of(day);
        week.setDays(weekDays);

        DayWorkoutsDTO dayDto = new DayWorkoutsDTO();
        dayDto.setName(day.getName());
        dayDto.setWorkout(modelMapper.map(workout, WorkoutDTO.class));
        dayDto.setCompleted(false);
        dayDto.setStarted(false);
        dayDto.setWorkout(workoutDto);

        when(weekService.getWeekByNumber(1L, 1L)).thenReturn(week);
        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(userProgressService.getUserProgressForProgramIdAndWeekId(1L, 1L, 1L)).thenReturn(userProgressList);
        when(modelMapper.map(week, ProgramWeekDTO.class)).thenReturn(programWeekDto);
        when(modelMapper.map(day, DayWorkoutsDTO.class)).thenReturn(dayDto);

        ProgramWeekDTO result = underTest.getWeekById(1L, 1L, "test");

        verify(weekService, times(1)).getWeekByNumber(1L, 1L);
        verify(userService, times(1)).getUserEntityByUsername("test");
        verify(userProgressService, times(1)).getUserProgressForProgramIdAndWeekId(1L, 1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.getNumber());
        assertEquals(1, result.getDays().size());

        DayWorkoutsDTO returnedDayDto = result.getDays().get(0);
        assertEquals("test day", returnedDayDto.getName());
        assertFalse(returnedDayDto.isCompleted());
        assertFalse(returnedDayDto.isStarted());

        assertEquals(day.getId(), returnedDayDto.getWorkout().getId());
        assertEquals(day.getWorkout().getName(), returnedDayDto.getWorkout().getName());
    }

}