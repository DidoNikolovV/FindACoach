package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.program.ProgramDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekWorkoutEntity;
import com.softuni.fitlaunch.model.entity.ProgramWorkoutExerciseEntity;
import com.softuni.fitlaunch.model.entity.WeekEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.repository.ProgramRepository;
import com.softuni.fitlaunch.repository.ProgramWeekRepository;
import com.softuni.fitlaunch.repository.ProgramWeekWorkoutRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    private final ProgramWeekRepository programWeekRepository;

    private final WorkoutService workoutService;

    private final ProgramWeekWorkoutRepository programWeekWorkoutRepository;


    private final ClientService clientService;


    private final ModelMapper modelMapper;


    public ProgramService(ProgramRepository programRepository, ProgramWeekRepository programWeekRepository, WorkoutService workoutService, ProgramWeekWorkoutRepository programWeekWorkoutRepository, ClientService clientService,  ModelMapper modelMapper) {
        this.programRepository = programRepository;
        this.programWeekRepository = programWeekRepository;
        this.workoutService = workoutService;
        this.programWeekWorkoutRepository = programWeekWorkoutRepository;
        this.clientService = clientService;
        this.modelMapper = modelMapper;
    }

    public List<ProgramDTO> loadAllProgramsByCoachId(Long coachId) {
        List<ProgramEntity> programEntities = programRepository.findAllByCoachId(coachId).orElseThrow(() -> new ObjectNotFoundException("Programs with coachId " + coachId + " not found"));
        return programEntities.stream().map(programEntity -> modelMapper.map(programEntity, ProgramDTO.class)).toList();
    }


    public WorkoutEntity getWorkoutEntityById(Long id) {
        return workoutService.getWorkoutEntityById(id);
    }

    public List<WeekEntity> getAllWeeksByProgramId(Long programId) {
        ProgramEntity programEntity = programRepository.findById(programId).orElseThrow(() -> new ObjectNotFoundException("Program with id " + programId + " does not exist"));
        return programEntity.getWeeks();
    }

    public ProgramWeekDTO getProgramWeekById(Long weekId) {
        ProgramWeekEntity programWeekEntity = programWeekRepository.findById(weekId).orElseThrow(() -> new ObjectNotFoundException("Week with id " + weekId + " was not found"));

        return modelMapper.map(programWeekEntity, ProgramWeekDTO.class);
    }

    public ProgramWeekWorkoutDTO getProgramWeekWorkoutById(Long id, UserDTO userDTO) {
        ProgramWeekWorkoutEntity programWeekWorkoutEntity = programWeekWorkoutRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Workout not found"));
        ClientEntity clientEntity = clientService.getClientEntityByUsername(userDTO.getUsername());

        List<ProgramWorkoutExerciseEntity> userProgramExercisesCompleted = clientEntity.getProgramExercisesCompleted();
        List<ProgramWorkoutExerciseEntity> programExercises = programWeekWorkoutEntity.getExercises();

        for (ProgramWorkoutExerciseEntity programExercise : programExercises) {
            if (userProgramExercisesCompleted.contains(programExercise)) {
                programExercise.setCompleted(true);
            }
        }

        return modelMapper.map(programWeekWorkoutEntity, ProgramWeekWorkoutDTO.class);
    }

    public ProgramDTO getById(Long programId) {
        ProgramEntity programEntity = programRepository.findById(programId).orElseThrow(() -> new ObjectNotFoundException("Program with id " + programId + " not found"));
        return modelMapper.map(programEntity, ProgramDTO.class);
    }


    public List<ProgramWeekWorkoutDTO> getAllWorkoutsByProgramId(Long programId) {
        List<ProgramWeekWorkoutEntity> programWeekWorkoutEntities = programWeekWorkoutRepository.findAllByProgramId(programId).orElseThrow(() -> new ObjectNotFoundException("Program with id " + programId + " not found"));
        return programWeekWorkoutEntities.stream().map(programWeekWorkoutEntity -> modelMapper.map(programWeekWorkoutEntity, ProgramWeekWorkoutDTO.class)).toList();
    }

    public void removeLike(WorkoutEntity weekWorkout) {
        Long oldLikes = Long.valueOf(weekWorkout.getLikes());
        if (oldLikes - 1 < 0) {
            weekWorkout.setLikes(0);
        } else {
            weekWorkout.setLikes((int) (oldLikes - 1));
        }
    }

}
