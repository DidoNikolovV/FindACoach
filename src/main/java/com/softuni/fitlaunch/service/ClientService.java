package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.repository.DailyMetricsRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final DailyMetricsRepository dailyMetricsRepository;

    private final ModelMapper modelMapper;


    public ClientService(ClientRepository clientRepository, DailyMetricsRepository dailyMetricsRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.dailyMetricsRepository = dailyMetricsRepository;
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

    public List<DailyMetricsDTO> calculateAverageWeeklyMetrics(String clientName) {
        ClientEntity client = getClientEntityByUsername(clientName);

        List<DailyMetricsEntity> metrics = dailyMetricsRepository.findAllByClientId(client.getId());

        return calculateAverage(metrics);
    }

    private List<DailyMetricsDTO> calculateAverage(List<DailyMetricsEntity> metrics) {
        List<DailyMetricsDTO> averageMetrics = new ArrayList<>();
        Double totalWeight = 0D;
        Double totalStepsCount = 0D;
        Double totalSleepDuration = 0D;
        Integer totalMood = 0;
        Integer totalEnergyLevels = 0;
        for (int i = 0; i < metrics.size(); i++) {
            totalWeight += metrics.get(i).getWeight();
            totalStepsCount += metrics.get(i).getStepsCount();
            totalSleepDuration += metrics.get(i).getSleepDuration();
            totalMood += metrics.get(i).getMood();
            totalEnergyLevels += metrics.get(i).getEnergyLevels();
        }

        DailyMetricsDTO dailyMetricsDTO = new DailyMetricsDTO();

        dailyMetricsDTO.setWeight(totalWeight / 2);
        dailyMetricsDTO.setStepsCount(totalStepsCount / 2);
        dailyMetricsDTO.setSleepDuration(totalSleepDuration / 2);
        dailyMetricsDTO.setMood(totalMood / 2);
        dailyMetricsDTO.setEnergyLevels(totalEnergyLevels / 2);

        averageMetrics.add(dailyMetricsDTO);

        return averageMetrics;
    }

    public double calculcateWeightProgress(Double clientWeight, Double weightGoal) {
        return Math.floor((clientWeight / weightGoal) * 100);
    }
}

