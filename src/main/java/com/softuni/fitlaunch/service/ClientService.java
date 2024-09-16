package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
import com.softuni.fitlaunch.model.entity.ProgressPicture;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.WeekMetricsEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.repository.DailyMetricsRepository;
import com.softuni.fitlaunch.repository.ProgressPictureRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final DailyMetricsRepository dailyMetricsRepository;

    private final WeekMetricsService weekMetricsService;

    private final ModelMapper modelMapper;

    private final FileUpload fileUpload;

    private final ProgressPictureRepository progressPictureRepository;


    public ClientService(ClientRepository clientRepository, DailyMetricsRepository dailyMetricsRepository, WeekMetricsService weekMetricsService, ModelMapper modelMapper, FileUpload fileUpload, ProgressPictureRepository progressPictureRepository) {
        this.clientRepository = clientRepository;
        this.dailyMetricsRepository = dailyMetricsRepository;
        this.weekMetricsService = weekMetricsService;
        this.modelMapper = modelMapper;
        this.fileUpload = fileUpload;
        this.progressPictureRepository = progressPictureRepository;
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
        List<DailyMetricsEntity> allByClientId = dailyMetricsRepository.findAllByClientId(clientEntity.getId());
        DailyMetricsEntity dailyMetrics = modelMapper.map(dailyWeightDTO, DailyMetricsEntity.class);
        WeekMetricsEntity weekMetricsEntity = null;
        boolean isNew = false;
        if (allByClientId.size() < 7) {
            weekMetricsEntity = weekMetricsService.getByNumberAndClientId(1, clientEntity.getId());
            if (weekMetricsEntity == null) {
                weekMetricsEntity = new WeekMetricsEntity();
                weekMetricsEntity.setNumber(1);
            }
        } else {
            DailyMetricsEntity last = allByClientId.get(allByClientId.size() - 1);
            WeekMetricsEntity lastWeek = last.getWeek();
            if (lastWeek.getDailyMetrics().size() >= 7) {
                weekMetricsEntity = new WeekMetricsEntity();
                weekMetricsEntity.setNumber(lastWeek.getNumber() + 1);
            } else {
                weekMetricsEntity = weekMetricsService.getByNumberAndClientId(lastWeek.getNumber(), clientEntity.getId());
            }
        }

        weekMetricsEntity.getDailyMetrics().add(dailyMetrics);
        dailyMetrics.setDate(LocalDate.now());
        dailyMetrics.setClient(clientEntity);
        dailyMetrics.setWeek(weekMetricsEntity);
        clientEntity.getDailyMetrics().add(dailyMetrics);

        weekMetricsEntity.setClient(clientEntity);
        weekMetricsService.saveWeekMetrics(weekMetricsEntity);
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
        Integer totalEnergyLevels = 0;
        for (int i = 0; i < metrics.size(); i++) {
            totalWeight += metrics.get(i).getWeight();
            totalStepsCount += metrics.get(i).getStepsCount();
            totalSleepDuration += metrics.get(i).getSleepDuration();
            totalEnergyLevels += metrics.get(i).getEnergyLevels();
        }

        DailyMetricsDTO dailyMetricsDTO = createDailyMetricsDto(totalWeight, totalStepsCount, totalSleepDuration, totalEnergyLevels);

        averageMetrics.add(dailyMetricsDTO);

        return averageMetrics;
    }

    private DailyMetricsDTO createDailyMetricsDto(Double totalWeight, Double totalStepsCount, Double totalSleepDuration, Integer totalEnergyLevels) {
        DailyMetricsDTO dailyMetricsDTO = new DailyMetricsDTO();

        dailyMetricsDTO.setWeight(totalWeight / 2);
        dailyMetricsDTO.setStepsCount(totalStepsCount / 2);
        dailyMetricsDTO.setSleepDuration(totalSleepDuration / 2);
        dailyMetricsDTO.setEnergyLevels(totalEnergyLevels / 2);
        return dailyMetricsDTO;
    }

    public double calculcateWeightProgress(Double clientWeight, Double weightGoal) {
        if (clientWeight == null) {
            return 0D;
        }
        return Math.floor((clientWeight / weightGoal) * 100);
    }

    public List<DailyMetricsDTO> getAllByWeekNumber(int weekNumber, String username) {
        ClientEntity client = getClientEntityByUsername(username);
        WeekMetricsEntity weekMetrics = weekMetricsService.getByNumberAndClientId(weekNumber, client.getId());
        return weekMetrics.getDailyMetrics().stream().map(metric -> modelMapper.map(metric, DailyMetricsDTO.class)).toList();
    }

    @Transactional
    public void addProgressPicture(String clientUsername, MultipartFile file) {
        ClientEntity client = getClientEntityByUsername(clientUsername);
        String fileUrl = fileUpload.uploadFile(file);

        ProgressPicture progressPicture = createProgressPicture(fileUrl, client);
        client.getProgressPictures().add(progressPicture);

        clientRepository.save(client);
    }

    private ProgressPicture createProgressPicture(String fileUrl, ClientEntity client) {
        ProgressPicture progressPicture = new ProgressPicture();
        progressPicture.setUrl(fileUrl);
        progressPicture.setClient(client);
        return progressPicture;
    }

    public Page<ProgressPicture> getProgressPicturesByClientUsername(String clientUsername, Pageable pageable) {
        ClientEntity client = getClientEntityByUsername(clientUsername);
        return progressPictureRepository.findByClientId(client.getId(), pageable);
    }

}

