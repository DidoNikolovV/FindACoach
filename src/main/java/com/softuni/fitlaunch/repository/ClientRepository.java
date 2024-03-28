package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByUsername(String username);

    List<ClientEntity> findAllByCoachId(Long coachId);

}
