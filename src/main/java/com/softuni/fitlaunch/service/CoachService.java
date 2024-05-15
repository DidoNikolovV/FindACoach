package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.CertificateDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.view.UserCoachDetailsView;
import com.softuni.fitlaunch.model.dto.view.UserCoachView;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.repository.CoachRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.softuni.fitlaunch.commons.ErrorMessages.CLIENT_WAS_NOT_FOUND;
import static com.softuni.fitlaunch.commons.ErrorMessages.COACH_DOES_NOT_EXIST;

@Service
public class CoachService {
    private final CoachRepository coachRepository;
    private final ModelMapper modelMapper;
    private final ClientService clientService;

    public CoachService(CoachRepository coachRepository, ModelMapper modelMapper, ClientService clientService) {
        this.coachRepository = coachRepository;
        this.modelMapper = modelMapper;
        this.clientService = clientService;
    }

    public Page<UserCoachView> getAllCoaches(Pageable pageable) {
        return coachRepository
                .findAll(pageable)
                .map(coachEntity -> modelMapper.map(coachEntity, UserCoachView.class));
    }

    public CoachDTO getCoachById(Long id) {
        CoachEntity coachEntity = coachRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(COACH_DOES_NOT_EXIST));
        List<CertificateDTO> coachCertificatesDTO = coachEntity.getCertificates().stream().map(certificateEntity -> modelMapper.map(certificateEntity, CertificateDTO.class)).toList();
        List<ClientDTO> coachClients = coachEntity.getClients().stream().map(clientEntity -> modelMapper.map(clientEntity, ClientDTO.class)).toList();
        List<ScheduledWorkoutDTO> scheduledWorkoutsDTO = coachEntity.getScheduledWorkouts().stream().map(scheduledWorkoutEntity -> modelMapper.map(scheduledWorkoutEntity, ScheduledWorkoutDTO.class)).toList();
        return new CoachDTO(coachEntity.getId(), coachEntity.getUsername(), coachEntity.getEmail(), coachEntity.getImgUrl(), coachEntity.getRating(), coachEntity.getDescription(), coachCertificatesDTO, new ArrayList<>(), coachClients, scheduledWorkoutsDTO);
    }


    public void addClient(Long coachId, ClientDTO client) {
        ClientEntity clientEntity = setClientGoals(client);
        CoachEntity coachEntity = coachRepository.findById(coachId).orElseThrow(() -> new ResourceNotFoundException(COACH_DOES_NOT_EXIST));

        coachEntity.getClients().add(clientEntity);
        clientEntity.setCoach(coachEntity);

        coachRepository.save(coachEntity);
    }

    private ClientEntity setClientGoals(ClientDTO client) {
        ClientEntity clientEntity = clientService.getClientEntityByUsername(client.getUsername());

        clientEntity.setWeight(client.getWeight());
        clientEntity.setWeightGoal(client.getWeightGoal());
        clientEntity.setPerformanceGoals(client.getPerformanceGoals());
        clientEntity.setBodyCompositionGoal(client.getPerformanceGoals());

        return clientEntity;
    }

    public UserCoachDetailsView getCoachDetailsById(Long id) {
        CoachEntity coachEntity = coachRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(COACH_DOES_NOT_EXIST));
        List<CertificateDTO> coachCertificatesDTO = coachEntity.getCertificates().stream().map(certificateEntity -> modelMapper.map(certificateEntity, CertificateDTO.class)).toList();
        return new UserCoachDetailsView(coachEntity.getUsername(), coachEntity.getEmail(), coachEntity.getImgUrl(), coachEntity.getRating(), coachEntity.getDescription(), coachCertificatesDTO);
    }

    public CoachDTO getCoachByUsername(String username) {
        CoachEntity coachEntity = coachRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(COACH_DOES_NOT_EXIST));
        return modelMapper.map(coachEntity, CoachDTO.class);
    }

    public ClientDTO getCoachClientById(CoachDTO coach, Long clientId) {
        CoachEntity coachEntity = coachRepository.findByUsername(coach.getUsername()).orElseThrow(() -> new ResourceNotFoundException(COACH_DOES_NOT_EXIST));

        ClientEntity client = coachEntity.getClients().stream().filter(c -> c.getId().equals(clientId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(CLIENT_WAS_NOT_FOUND));

        ClientEntity clientEntity = clientService.getClientEntityByUsername(client.getUsername());
        return modelMapper.map(clientEntity, ClientDTO.class);
    }

    public void registerCoach(UserEntity user) {
        CoachEntity coach = modelMapper.map(user, CoachEntity.class);
        coachRepository.save(coach);
    }

    public CoachEntity getCoachEntityByUsername(String username) {
        return coachRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(CLIENT_WAS_NOT_FOUND));
    }

    public CoachEntity getCoachEntityById(Long id) {
        return coachRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(CLIENT_WAS_NOT_FOUND));

    }

    public void setClientDetails(Long id, ClientDTO clientDTO) {
        CoachEntity coach = getCoachEntityById(id);
        ClientEntity client = modelMapper.map(clientDTO, ClientEntity.class);
        coach.getClients().add(client);

        coachRepository.save(coach);
    }
}
