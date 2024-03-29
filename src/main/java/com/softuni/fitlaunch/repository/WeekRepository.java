package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.WeekEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekRepository extends JpaRepository<WeekEntity, Long> {
    List<WeekEntity> findAllByProgramId(Long programId);

}
