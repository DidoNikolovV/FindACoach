package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.CertificateDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.ClientDetailsDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.view.UserCoachDetailsView;
import com.softuni.fitlaunch.model.dto.view.UserCoachView;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.repository.ClientRepository;
import com.softuni.fitlaunch.repository.CoachRepository;
import com.softuni.fitlaunch.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoachService {
    private final CoachRepository coachRepository;

    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;

    public CoachService(CoachRepository coachRepository, ModelMapper modelMapper, ClientRepository clientRepository) {
        this.coachRepository = coachRepository;
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
    }

    public List<UserCoachView> getAllCoaches() {
        List<CoachEntity> coachesEntity = coachRepository.findAll();

        return coachesEntity.stream().map(coachEntity -> new UserCoachView(coachEntity.getId(), coachEntity.getImgUrl(), coachEntity.getUsername(), coachEntity.getEmail(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In rhoncus enim at enim bibendum porta. Suspendisse id mollis neque, et sodales nisi. Ut eget pulvinar felis. Curabitur sed suscipit nibh, at scelerisque arcu. Cras pharetra sodales ultrices. Vestibulum aliquet elementum libero vel congue. Mauris ut quam ultrices odio posuere efficitur. Phasellus sagittis pellentesque laoreet.", coachEntity.getRating())).toList();

    }

    public CoachDTO getCoachById(Long id) {
        CoachEntity coachEntity = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Coach does not exist"));
        List<CertificateDTO> coachCertificatesDTO = coachEntity.getCertificates().stream().map(certificateEntity -> modelMapper.map(certificateEntity, CertificateDTO.class)).toList();
        List<ClientDTO> coachClients = coachEntity.getClients().stream().map(clientEntity -> modelMapper.map(clientEntity, ClientDTO.class)).toList();
        List<ScheduledWorkoutDTO> scheduledWorkoutsDTO = coachEntity.getScheduledWorkouts().stream().map(scheduledWorkoutEntity -> modelMapper.map(scheduledWorkoutEntity, ScheduledWorkoutDTO.class)).toList();
        return new CoachDTO(coachEntity.getId(), coachEntity.getUsername(), coachEntity.getEmail(), coachEntity.getImgUrl(), coachEntity.getRating(), coachEntity.getDescription(), coachCertificatesDTO, new ArrayList<>(), coachClients, scheduledWorkoutsDTO);
    }

    public ClientDTO getClientByUsername(String username) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client not found"));
        return modelMapper.map(clientEntity, ClientDTO.class);
    }


    public void addClient(Long coachId, ClientDTO client) {
        ClientEntity clientEntity = clientRepository.findByUsername(client.getUsername()).orElseThrow(() -> new ObjectNotFoundException("Client not found"));
        CoachEntity coachEntity = coachRepository.findById(coachId).orElseThrow(() -> new ObjectNotFoundException("Coach not found"));

        coachEntity.getClients().add(clientEntity);
        clientEntity.setCoach(coachEntity);

        coachRepository.save(coachEntity);
        clientRepository.save(clientEntity);
    }


    public UserCoachDetailsView getCoachDetailsById(Long id) {
        CoachEntity coachEntity = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Coach does not exist"));
        List<CertificateDTO> coachCertificatesDTO = coachEntity.getCertificates().stream().map(certificateEntity -> modelMapper.map(certificateEntity, CertificateDTO.class)).toList();
        return new UserCoachDetailsView(coachEntity.getUsername(), coachEntity.getEmail(), coachEntity.getImgUrl(), coachEntity.getRating(), coachEntity.getDescription(), coachCertificatesDTO);
    }

    public CoachDTO getCoachByUsername(String username) {
        CoachEntity coachEntity = coachRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Coach does not exist"));
        return modelMapper.map(coachEntity, CoachDTO.class);
    }

    public ClientDTO getCoachClientById(CoachDTO coach, Long clientId) {
        CoachEntity coachEntity = coachRepository.findByUsername(coach.getUsername()).orElseThrow(() -> new ObjectNotFoundException("Coach with username " + coach.getUsername() + " was not found"));

        Optional<ClientEntity> optionalClient = coachEntity.getClients().stream().filter(client -> client.getId().equals(clientId)).findFirst();
        if (optionalClient.isPresent()) {
            ClientEntity clientEntity = clientRepository.findByUsername(optionalClient.get().getUsername()).get();
            return modelMapper.map(clientEntity, ClientDTO.class);
        } else {
            throw new ObjectNotFoundException("Client was not found");
        }
    }

    public void setClientDetails(String username, ClientDetailsDTO clientDetailsDTO) {
        ClientEntity clientEntity = clientRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Client not found"));
        BeanUtils.copyProperties(clientDetailsDTO, clientEntity);

        clientRepository.save(clientEntity);
    }

    public void registerCoach(UserEntity user) {
        CoachEntity coach = modelMapper.map(user, CoachEntity.class);
        coachRepository.save(coach);
    }
}
