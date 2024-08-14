package com.softuni.fitlaunch.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

class ImageServiceTest {

    @Mock
    private FileUpload fileUpload;

    @InjectMocks
    private ImageService underTest;

    public ImageServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateImage() {
        MultipartFile mock = Mockito.mock(MultipartFile.class);

        underTest.createImage(mock);

    }
}