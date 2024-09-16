package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.WeekMetricsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeekMetricsRepository extends JpaRepository<WeekMetricsEntity, Long> {
    Optional<WeekMetricsEntity> findByNumber(int number);

    Optional<WeekMetricsEntity> findByNumberAndClientId(int number, Long clientId);

}
