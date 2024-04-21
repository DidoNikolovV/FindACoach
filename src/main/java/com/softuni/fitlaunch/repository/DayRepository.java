package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.DayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayRepository extends JpaRepository <DayEntity, Long> {
    List<DayEntity> findAllByWeekId(Long weekId);
}
