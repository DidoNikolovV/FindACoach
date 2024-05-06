package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
import com.softuni.fitlaunch.model.entity.DailyWeightEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Client with username " + username + " was not found"));
        return modelMapper.map(clientEntity, ClientDTO.class);
    }

    public ClientEntity getClientEntityByUsername(String username) {
        return clientRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Client with username " + username + " was not found"));
    }

    public List<ClientDTO> loadAllByCoach(CoachDTO coach) {
        return clientRepository.findAllByCoachId(coach.getId()).stream().map(client -> modelMapper.map(client, ClientDTO.class)).collect(Collectors.toList());
    }

    public void saveDailyMetrics(String clientName, DailyMetricsDTO dailyWeightDTO) {
        ClientEntity clientEntity = getClientEntityByUsername(clientName);

        DailyMetricsEntity dailyMetrics = modelMapper.map(dailyWeightDTO, DailyMetricsEntity.class);
        dailyMetrics.setDate(LocalDate.now());
        dailyMetrics.setClient(clientEntity);
        clientEntity.getDailyMetrics().add(dailyMetrics);

        clientRepository.save(clientEntity);
    }
}

