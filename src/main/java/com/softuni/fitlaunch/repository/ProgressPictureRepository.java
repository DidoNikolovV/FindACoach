package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.ProgressPicture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressPictureRepository extends JpaRepository<ProgressPicture, Long> {

    Page<ProgressPicture> findByClientId(Long clientId, Pageable pageable);
}
