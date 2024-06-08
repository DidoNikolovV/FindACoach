package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.dto.TopicCommentDTO;
import com.softuni.fitlaunch.model.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByWorkoutId(Long workoutId);

    Page<CommentEntity> findAllByTopicId(Long id, PageRequest pageRequest);
}
