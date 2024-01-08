package com.softuni.fitlaunch.service;



import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.entity.*;
import com.softuni.fitlaunch.repository.*;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    private final ProgramWeekRepository programWeekRepository;

    private final ProgramWeekWorkoutRepository programWeekWorkoutRepository;

    private final UserRepository userRepository;

    private final CoachRepository coachRepository;
    private final ClientRepository clientRepository;

    private final ModelMapper modelMapper;


    public ProgramService(ProgramRepository programRepository, ProgramWeekRepository programWeekRepository, ProgramWeekWorkoutRepository programWeekWorkoutRepository, UserRepository userRepository, CoachRepository coachRepository, ClientRepository clientRepository, ModelMapper modelMapper) {
        this.programRepository = programRepository;
        this.programWeekRepository = programWeekRepository;
        this.programWeekWorkoutRepository = programWeekWorkoutRepository;
        this.userRepository = userRepository;
        this.coachRepository = coachRepository;
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<ProgramDTO> loadAllPrograms() {
        return programRepository.findAll().stream().map(programEntity -> modelMapper.map(programEntity, ProgramDTO.class)).toList();
    }

    public List<ProgramWeekDTO> getAllWeeksByProgramId(Long programId, ClientDTO clientDTO) {
        List<ProgramWeekEntity> programWeeks = programWeekRepository.findAllByProgramId(programId).orElseThrow(() -> new ObjectNotFoundException("Program with id " + programId + " was not found"));
        ClientEntity clientEntity = clientRepository.findByUsername(clientDTO.getUsername()).orElseThrow(() -> new ObjectNotFoundException("Client with " + clientDTO.getUsername() + " was not found"));

        List<ProgramWeekWorkoutEntity> workoutsCompleted = clientEntity.getCompletedWorkouts();


        for (ProgramWeekEntity programWeek : programWeeks) {
            for (ProgramWeekWorkoutEntity weekWorkout : programWeek.getWeekWorkouts()) {
                if(workoutsCompleted.contains(weekWorkout)) {
                    weekWorkout.setCompleted(true);
                }
            }
        }


        List<ProgramWeekDTO> programWeeksDTO = programWeeks.stream().map(programWeekEntity -> modelMapper.map(programWeekEntity, ProgramWeekDTO.class)).toList();

        return programWeeksDTO;
    }

    public ProgramWeekDTO getProgramWeekById(Long weekId) {
        ProgramWeekEntity programWeekEntity = programWeekRepository.findById(weekId).orElseThrow(() -> new ObjectNotFoundException("Week with id " + weekId + " was not found"));

        ProgramWeekDTO programWeekWorkoutDTO = modelMapper.map(programWeekEntity, ProgramWeekDTO.class);

        return programWeekWorkoutDTO;

    }

    public ProgramWeekWorkoutDTO getProgramWeekWorkoutById(Long id, UserDTO userDTO) {
        ProgramWeekWorkoutEntity programWeekWorkoutEntity = programWeekWorkoutRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Workout not found"));
        UserEntity userEntity = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User with " + userDTO.getUsername() + " not found"));

        List<ProgramWorkoutExerciseEntity> userProgramExercisesCompleted = userEntity.getProgramExercisesCompleted();
        List<ProgramWorkoutExerciseEntity> programExercises = programWeekWorkoutEntity.getExercises();

        for (ProgramWorkoutExerciseEntity programExercise : programExercises) {
            if(userProgramExercisesCompleted.contains(programExercise)) {
                programExercise.setCompleted(true);
            }
        }

        ProgramWeekWorkoutDTO programWeekWorkoutDTO = modelMapper.map(programWeekWorkoutEntity, ProgramWeekWorkoutDTO.class);

        return programWeekWorkoutDTO;
    }

    public ProgramDTO getById(Long programId) {
        ProgramEntity programEntity = programRepository.findById(programId).orElseThrow(() -> new ObjectNotFoundException("Program with id " + programId + " not found"));
        return modelMapper.map(programEntity, ProgramDTO.class);
    }

    public ProgramWeekWorkoutDTO getProgramWorkout(Long programId, Long weekId, Long workoutId, UserDTO userDTO) {
        ProgramWeekWorkoutEntity programWeekWorkout = programWeekWorkoutRepository.findByProgramWeekIdAndId(weekId, workoutId).orElseThrow(() -> new ObjectNotFoundException("Workout was not found"));
        ProgramWeekWorkoutDTO programWeekWorkoutDTO = modelMapper.map(programWeekWorkout, ProgramWeekWorkoutDTO.class);

        programWeekWorkout.setHasStarted(true);
        UserEntity user = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User with " + userDTO.getUsername() + " doesn't exist"));
        user.getWorkoutsStarted().add(programWeekWorkout);

        programWeekWorkoutRepository.save(programWeekWorkout);
        userRepository.save(user);

        return programWeekWorkoutDTO;
    }


    public List<ProgramDTO> loadAllProgramsByCoachId(Long coachId) {
        List<ProgramEntity> programEntities = programRepository.findAllByCoachId(coachId).orElseThrow(() -> new ObjectNotFoundException("Programs with coachId " + coachId + " not found"));
        List<ProgramDTO> programDTOS = programEntities.stream().map(programEntity -> modelMapper.map(programEntity, ProgramDTO.class)).toList();
        return programDTOS;
    }

    public List<ProgramWeekWorkoutDTO> getAllWorkoutsByProgramId(Long programId) {
        List<ProgramWeekWorkoutEntity> programWeekWorkoutEntities = programWeekWorkoutRepository.findAllByProgramId(programId).orElseThrow(() -> new ObjectNotFoundException("Program with id " + programId + " not found"));
        List<ProgramWeekWorkoutDTO> programWeekWorkoutDTOs = programWeekWorkoutEntities.stream().map(programWeekWorkoutEntity -> modelMapper.map(programWeekWorkoutEntity, ProgramWeekWorkoutDTO.class)).toList();
        return programWeekWorkoutDTOs;
    }
}
