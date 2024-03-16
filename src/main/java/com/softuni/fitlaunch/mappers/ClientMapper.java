package com.softuni.fitlaunch.mappers;

import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClientMapper {

    public ClientEntity mapUserToClient(UserEntity user) {
        ClientEntity clientEntity = new ClientEntity();
        BeanUtils.copyProperties(user, clientEntity);
        clientEntity.setCoach(null);
        clientEntity.setWorkoutStarted(null);
        clientEntity.setProgramExercisesCompleted(new ArrayList<>());
        clientEntity.setScheduledWorkouts(new ArrayList<>());
        clientEntity.setCompletedPrograms(new ArrayList<>());
        clientEntity.setCompletedWorkouts(new ArrayList<>());

        return clientEntity;
    }

    public ClientDTO mapToDTO(ClientEntity client) {
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client, clientDTO);

        return clientDTO;
    }
}
