package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.WeekEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeekRepository extends JpaRepository<WeekEntity, Long> {
    List<WeekEntity> findAllByProgramId(Long programId);

    Optional<WeekEntity> findByNumber(int number);
}
