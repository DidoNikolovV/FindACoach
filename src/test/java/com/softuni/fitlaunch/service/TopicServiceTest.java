package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.TopicDTO;
import com.softuni.fitlaunch.model.entity.TopicEntity;
import com.softuni.fitlaunch.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TopicServiceTest {
    @Mock
    private TopicRepository topicRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TopicService underTest;

    public TopicServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTopics_whenTopicsExistsInDatabase_thenReturnPageOfThem() {
        Page topics = Mockito.mock(Page.class);

        when(topicRepository.findAll((Pageable) any())).thenReturn(topics);

        underTest.getAllTopics(any());
    }

    @Test
    void testSaveTopic_whenNewTopicIsCreated_thenCreateAndPersistInDatabase() {
        TopicDTO topicDto = new TopicDTO();
        topicDto.setId(1L);
        topicDto.setTitle("title");
        topicDto.setContent("content");
        topicDto.setAuthor("author");

        TopicEntity topic = new TopicEntity();
        topic.setId(topicDto.getId());
        topic.setAuthor(topicDto.getAuthor());
        topic.setContent(topicDto.getContent());
        topic.setTitle(topicDto.getTitle());


        when(modelMapper.map(topicDto, TopicEntity.class)).thenReturn(topic);
        when(topicRepository.save(topic)).thenReturn(topic);

        underTest.saveTopic(topicDto);

        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void testGetById_whenTopicExistsInDatabase_thenReturnIt() {
        TopicDTO topicDto = new TopicDTO();
        topicDto.setId(1L);
        topicDto.setTitle("title");
        topicDto.setContent("content");
        topicDto.setAuthor("author");

        TopicEntity topic = new TopicEntity();
        topic.setId(topicDto.getId());
        topic.setAuthor(topicDto.getAuthor());
        topic.setContent(topicDto.getContent());
        topic.setTitle(topicDto.getTitle());


        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(modelMapper.map(topic, TopicDTO.class)).thenReturn(topicDto);

        underTest.getById(1L);

        verify(topicRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEntityById_whenTopicExistsInDatabase_thenReturnIt() {
        TopicEntity topic = new TopicEntity();
        topic.setId(1L);
        topic.setAuthor("author");
        topic.setContent("content");
        topic.setTitle("title");

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        underTest.getEntityById(1L);

        verify(topicRepository, times(1)).findById(1L);
    }



}