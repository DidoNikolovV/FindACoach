package com.softuni.fitlaunch.web.rest;

import com.softuni.fitlaunch.model.dto.user.DailyMetricsDTO;
import com.softuni.fitlaunch.model.dto.workout.ClientWorkoutDetails;
import com.softuni.fitlaunch.service.ClientService;
import com.softuni.fitlaunch.service.WorkoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/clients")
public class ClientDetailsRestController {
    private final ClientService clientService;

    private final WorkoutService workoutService;

    public ClientDetailsRestController(ClientService clientService, WorkoutService workoutService) {
        this.clientService = clientService;
        this.workoutService = workoutService;
    }

    @GetMapping("/completed-workouts/{clientName}/workouts/{workoutId}/{dayName}/details")
    public ResponseEntity<ClientWorkoutDetails> loadCompletedWorkoutDetails(@PathVariable("clientName") String clientName,
                                                                            @PathVariable("workoutId") Long workoutId,
                                                                            @PathVariable("dayName") String dayName) {
        ClientWorkoutDetails workoutDetailsByIdForClient = workoutService.getWorkoutDetailsByIdForClient(workoutId, clientName, dayName);

        return ResponseEntity.ok(workoutDetailsByIdForClient);
    }

    @GetMapping("{clientName}/metrics/data")
    public ResponseEntity<List<DailyMetricsDTO>> loadMetricsData(@PathVariable("clientName") String clientName) {
        List<DailyMetricsDTO> metrics = clientService.calculateAverageWeeklyMetrics(clientName);

        return ResponseEntity.ok(metrics);
    }
}
