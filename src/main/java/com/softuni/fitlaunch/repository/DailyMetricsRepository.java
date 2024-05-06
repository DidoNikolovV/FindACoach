package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.DailyMetricsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyMetricsRepository extends JpaRepository<DailyMetricsEntity, Long> {
}
