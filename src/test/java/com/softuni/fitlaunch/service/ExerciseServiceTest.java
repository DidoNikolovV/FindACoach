package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import com.softuni.fitlaunch.repository.ExerciseRepository;
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

class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExerciseService underTest;

    public ExerciseServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLoadAllExercises_whenExercisesExists_thenReturnPageOfThem() {
        Page page = Mockito.mock(Page.class);
        when(exerciseRepository.findAll((Pageable) any())).thenReturn(page);

        underTest.loadAllExercises(any());
    }

    @Test
    void testGetByName_whenExistsWithThisName_thenReturnThisExercise() {
        ExerciseEntity exercise = new ExerciseEntity();
        exercise.setName("Bench Press");
        when(exerciseRepository.findByName("Bench Press")).thenReturn(Optional.of(exercise));

        underTest.getByName("Bench Press");

        verify(exerciseRepository, times(1)).findByName("Bench Press");
    }

}