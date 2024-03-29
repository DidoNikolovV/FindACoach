package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final ModelMapper modelMapper;


    public ClientService(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public void registerClient(UserEntity user) {
        ClientEntity client = modelMapper.map(user, ClientEntity.class);
        clientRepository.save(client);
    }

    public ClientDTO getClientByUsername(String username) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
        return modelMapper.map(clientEntity, ClientDTO.class);
    }

    public ClientEntity getClientEntityByUsername(String username) {
        return clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client with username " + username + " was not found"));
    }


    public boolean isWorkoutCompleted(String username, Long workoutId) {
        ClientEntity clientEntity = getClientEntityByUsername(username);

        return clientEntity.getCompletedWorkouts().stream()
                .anyMatch(weekWorkoutDTO -> weekWorkoutDTO.getId().equals(workoutId));
    }

    public List<ClientDTO> loadAllByCoach(CoachDTO coach) {
        return clientRepository.findAllByCoachId(coach.getId()).stream().map(client -> modelMapper.map(client, ClientDTO.class)).collect(Collectors.toList());
    }
}
