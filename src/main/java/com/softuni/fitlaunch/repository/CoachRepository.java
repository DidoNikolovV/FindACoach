package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.CoachEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<CoachEntity, Long> {
    Optional<CoachEntity> findByUsername(String username);
}
