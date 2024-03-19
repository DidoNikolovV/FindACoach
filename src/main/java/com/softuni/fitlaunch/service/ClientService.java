package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.mappers.ClientMapper;
import com.softuni.fitlaunch.mappers.UserMapper;
import com.softuni.fitlaunch.mappers.WorkoutMapper;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;

    private final WorkoutMapper workoutMapper;

    public ClientService(ClientRepository clientRepository, UserMapper userMapper, ClientMapper clientMapper, WorkoutMapper workoutMapper) {
        this.clientRepository = clientRepository;
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
        this.workoutMapper = workoutMapper;
    }

    public void registerClient(UserEntity user) {
        ClientEntity client = userMapper.mapUserToClient(user);
        clientRepository.save(client);
    }

    public ClientDTO getClientByUsername(String username) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
        return clientMapper.mapToDTO(clientEntity);
    }

    public ClientEntity getClientEntityByUsername(String username) {
        return clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
    }

    public boolean isWorkoutStarted(String username, Long workoutId) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
        return clientEntity.getWorkoutStarted().isHasStarted();
    }


    public boolean isWorkoutCompleted(String username, Long workoutId) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));

        return clientEntity.getCompletedWorkouts().stream()
                .anyMatch(weekWorkoutDTO -> weekWorkoutDTO.getId().equals(workoutId));
    }

    @Transactional
    public List<WorkoutDTO> getCompletedWorkouts(String username) {
        ClientEntity clientEntity = getClientEntityByUsername(username);

        return clientEntity.getCompletedWorkouts().stream().map(workoutMapper::mapToDTO).toList();
    }
}
