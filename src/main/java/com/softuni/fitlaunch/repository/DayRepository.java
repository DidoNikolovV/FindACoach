package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.DayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository <DayEntity, Long> {
}
