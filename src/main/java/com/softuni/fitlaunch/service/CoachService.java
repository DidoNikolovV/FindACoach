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

    public List<UserCoachView> getAllCoaches() {
        List<CoachEntity> coachesEntity = coachRepository.findAll();

        return coachesEntity.stream().map(coachEntity -> new UserCoachView(coachEntity.getId(), coachEntity.getImgUrl(), coachEntity.getUsername(), coachEntity.getEmail(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In rhoncus enim at enim bibendum porta. Suspendisse id mollis neque, et sodales nisi. Ut eget pulvinar felis. Curabitur sed suscipit nibh, at scelerisque arcu. Cras pharetra sodales ultrices. Vestibulum aliquet elementum libero vel congue. Mauris ut quam ultrices odio posuere efficitur. Phasellus sagittis pellentesque laoreet.", coachEntity.getRating())).toList();

    }

    public CoachDTO getCoachById(Long id) {
        CoachEntity coachEntity = coachRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(COACH_DOES_NOT_EXIST));
        List<CertificateDTO> coachCertificatesDTO = coachEntity.getCertificates().stream().map(certificateEntity -> modelMapper.map(certificateEntity, CertificateDTO.class)).toList();
        List<ClientDTO> coachClients = coachEntity.getClients().stream().map(clientEntity -> modelMapper.map(clientEntity, ClientDTO.class)).toList();
        List<ScheduledWorkoutDTO> scheduledWorkoutsDTO = coachEntity.getScheduledWorkouts().stream().map(scheduledWorkoutEntity -> modelMapper.map(scheduledWorkoutEntity, ScheduledWorkoutDTO.class)).toList();
        return new CoachDTO(coachEntity.getId(), coachEntity.getUsername(), coachEntity.getEmail(), coachEntity.getImgUrl(), coachEntity.getRating(), coachEntity.getDescription(), coachCertificatesDTO, new ArrayList<>(), coachClients, scheduledWorkoutsDTO);
    }


    public void addClient(Long coachId, ClientDTO client) {
        ClientEntity clientEntity = clientService.getClientEntityByUsername(client.getUsername());
        CoachEntity coachEntity = coachRepository.findById(coachId).orElseThrow(() -> new ResourceNotFoundException(COACH_DOES_NOT_EXIST));

        coachEntity.getClients().add(clientEntity);
        clientEntity.setCoach(coachEntity);

        coachRepository.save(coachEntity);
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
}
