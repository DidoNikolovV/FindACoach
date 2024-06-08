package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.TopicDTO;
import com.softuni.fitlaunch.model.entity.TopicEntity;
import com.softuni.fitlaunch.repository.TopicRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

    public TopicService(TopicRepository topicRepository, ModelMapper modelMapper) {
        this.topicRepository = topicRepository;
        this.modelMapper = modelMapper;
    }

    public Page<TopicEntity> getAllTopics(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    public TopicEntity saveTopic(TopicDTO topicDTO) {
        TopicEntity newTopic = modelMapper.map(topicDTO, TopicEntity.class);

        newTopic = topicRepository.save(newTopic);

        return newTopic;
    }

    public TopicDTO getById(Long id) {
        return modelMapper.map(topicRepository.findById(id).orElse(null), TopicDTO.class);
    }

    public TopicEntity getEntityById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }
}
