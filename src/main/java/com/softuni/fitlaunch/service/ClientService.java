package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekWorkoutEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.repository.ProgramWeekWorkoutRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final ProgramWeekWorkoutRepository programWeekWorkoutRepository;

    private final ModelMapper modelMapper;

    public ClientService(ClientRepository clientRepository, ProgramWeekWorkoutRepository programWeekWorkoutRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.programWeekWorkoutRepository = programWeekWorkoutRepository;
        this.modelMapper = modelMapper;
    }

    public ClientDTO registerClient(UserEntity userEntity) {
        ClientEntity clientEntity = modelMapper.map(userEntity, ClientEntity.class);
        clientEntity.setCoach(null);
        clientRepository.save(clientEntity);
        return modelMapper.map(clientEntity, ClientDTO.class);
    }

    public ClientDTO getClientByUsername(String username) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
        return modelMapper.map(clientEntity, ClientDTO.class);
    }

    public void completedProgramWorkout(ClientDTO client, ProgramWeekWorkoutDTO programWorkout) {
        ClientEntity clientEntity = clientRepository.findByUsername(client.getUsername()).orElseThrow(() -> new ObjectNotFoundException("Client with username " + client.getUsername() + " was not found"));
        ProgramWeekWorkoutEntity programWeekWorkoutEntity = programWeekWorkoutRepository.findById(programWorkout.getId()).orElseThrow(() -> new ObjectNotFoundException("Workout with id " + programWorkout.getId() + " was not found"));

        clientEntity.getCompletedWorkouts().add(programWeekWorkoutEntity);
        programWeekWorkoutRepository.save(programWeekWorkoutEntity);
        clientRepository.save(clientEntity);
    }
}
