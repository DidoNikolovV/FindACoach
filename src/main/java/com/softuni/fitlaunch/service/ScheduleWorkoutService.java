package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.user.ClientDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.view.ScheduledWorkoutView;
import com.softuni.fitlaunch.model.entity.ClientEntity;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.ScheduledWorkoutEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import com.softuni.fitlaunch.repository.ScheduledWorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleWorkoutService {

    private final ScheduledWorkoutRepository scheduledWorkoutRepository;

    private final CoachService coachService;
    private final ClientService clientService;

    private final UserService userService;

    public ScheduleWorkoutService(ScheduledWorkoutRepository scheduledWorkoutRepository, CoachService coachService, ClientService clientService, UserService userService) {
        this.scheduledWorkoutRepository = scheduledWorkoutRepository;
        this.coachService = coachService;
        this.clientService = clientService;
        this.userService = userService;
    }

    public void scheduleWorkout(ClientDTO clientDTO, CoachDTO coachDTO, LocalDateTime scheduledTime) {
        ClientEntity clientEntity = clientService.getClientEntityByUsername(clientDTO.getUsername());
        CoachEntity coachEntity = coachService.getCoachEntityByUsername(coachDTO.getUsername());
        ScheduledWorkoutEntity scheduledWorkoutEntity = new ScheduledWorkoutEntity();
        scheduledWorkoutEntity.setClient(clientEntity);
        scheduledWorkoutEntity.setCoach(coachEntity);
        scheduledWorkoutEntity.setScheduledDateTime(scheduledTime);

        scheduledWorkoutRepository.save(scheduledWorkoutEntity);

    }


    public List<ScheduledWorkoutView> getAllScheduledWorkouts(String username) {
        UserEntity userEntity = userService.getUserEntityByUsername(username);
        List<ScheduledWorkoutEntity> allScheduledWorkouts;
        if (userEntity.getTitle().equals(UserTitleEnum.CLIENT)) {
            ClientEntity client = clientService.getClientEntityByUsername(username);
            allScheduledWorkouts = scheduledWorkoutRepository.findAllByClientId(client.getId());
        } else {
            CoachEntity coach = coachService.getCoachEntityByUsername(username);
            allScheduledWorkouts = scheduledWorkoutRepository.findAllByCoachId(coach.getId());
        }

        return allScheduledWorkouts.stream().map(scheduledWorkoutEntity -> new ScheduledWorkoutView(scheduledWorkoutEntity.getId(), scheduledWorkoutEntity.getClient().getUsername(), scheduledWorkoutEntity.getCoach().getUsername(), scheduledWorkoutEntity.getScheduledDateTime().toString())).toList();
    }


    @Transactional
    public void deleteScheduledWorkout(Long eventId) {
        scheduledWorkoutRepository.deleteById(eventId);
    }

}
