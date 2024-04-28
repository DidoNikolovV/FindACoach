package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.DailyWeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeightRepository extends JpaRepository<DailyWeightEntity, Long> {
}
