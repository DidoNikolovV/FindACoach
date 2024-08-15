package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.repository.WeekRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeekServiceTest {

    @Mock
    private WeekRepository weekRepository;

    @InjectMocks
    private WeekService underTest;

    public WeekServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetWeekByNumber_whenWeekWithThisNumberExists_thenReturnIt() {
        ProgramEntity program = new ProgramEntity();
        program.setId(1L);

        ProgramWeekEntity week = new ProgramWeekEntity();
        week.setId(1L);
        week.setNumber(1);
        week.setProgram(program);

        when(weekRepository.findByNumberAndProgramId(1L, 1L)).thenReturn(Optional.of(week));

        underTest.getWeekByNumber(1L, 1L);

        verify(weekRepository, times(1)).findByNumberAndProgramId(1L, 1L);
    }

}