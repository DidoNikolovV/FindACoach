package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.entity.BlacklistEntity;
import com.softuni.fitlaunch.repository.BlacklistRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BlackListServiceTest {

    @Mock
    private BlacklistRepository blacklistRepository;

    @InjectMocks
    private BlackListService underTest;

    public BlackListServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testBanUser_whenUserIsNotBanned_thenBanHim() {
        underTest.banUser("address");
        verify(blacklistRepository, times(1)).save(any());
    }

    @Test
    void testUnBanUser_whenUserIsBanned_thenUnbanHim() {
        BlacklistEntity blacklistEntity = new BlacklistEntity();
        blacklistEntity.setIpAddress("address");
        blacklistEntity.setId(1L);

        when(blacklistRepository.findByIpAddress("address")).thenReturn(Optional.of(blacklistEntity));
        underTest.unbanUser("address");

        verify(blacklistRepository, times(1)).delete(blacklistEntity);
    }

    @Test
    void testIsBanned_whenUserIsBanned_thenReturnTrue() {
        BlacklistEntity blacklistEntity = new BlacklistEntity();
        blacklistEntity.setIpAddress("address");
        blacklistEntity.setId(1L);

        when(blacklistRepository.findByIpAddress("address")).thenReturn(Optional.of(blacklistEntity));

        underTest.isBanned("address");
    }

}