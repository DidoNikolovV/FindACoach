package com.softuni.fitlaunch.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileUploadTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private FileUpload underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void testUploadFile_success() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test-image-content".getBytes()
        );

        String imageId = "1234-5678-9012";
        when(uploader.upload(any(File.class), any(Map.class))).thenReturn(Map.of());

        underTest.uploadFile(mockMultipartFile);

        Path tempFilePath = Paths.get(imageId);
        if (Files.exists(tempFilePath)) {
            Files.delete(tempFilePath);
        }

        verify(uploader, times(1)).upload(any(File.class), any(Map.class));
    }

    @Test
    void testGetFileExtension() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "test-image-content".getBytes()
        );

        String result = underTest.uploadFile(mockMultipartFile);

        String expectedExtension = "png";
        String fileExtension = result.substring(result.lastIndexOf('.') + 1);

        assertEquals(expectedExtension, fileExtension);
    }

}