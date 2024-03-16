package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.mappers.ClientMapper;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekWorkoutEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.repository.ProgramWeekWorkoutRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final WorkoutService workoutService;

    private final ModelMapper modelMapper;

    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, WorkoutService workoutService, ModelMapper modelMapper, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.workoutService = workoutService;
        this.modelMapper = modelMapper;
        this.clientMapper = clientMapper;
    }

    public void registerClient(UserEntity user) {
        ClientEntity client = clientMapper.mapUserToClient(user);
        clientRepository.save(client);
    }

    public ClientDTO getClientByUsername(String username) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
        return modelMapper.map(clientEntity, ClientDTO.class);
    }

    public ClientEntity getClientEntityByUsername(String username) {
        return clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
    }

//    public void startWorkout(String username, Long workoutId) {
//        WorkoutEntity workout = workoutService.getWorkoutEntityById(workoutId);
//        ClientEntity client = getClientEntityByUsername(username);
//        client.setWorkoutStarted(workout);
//        client.setWorkoutStarted(workout);
//        clientRepository.save(client);
//    }
}
