package com.softuni.fitlaunch.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUpload {

    private final Cloudinary cloudinary;


    @SneakyThrows
    public String uploadFile(MultipartFile multipartFile) {
        String imageId = UUID.randomUUID().toString();
        Map params = ObjectUtils.asMap(
                "public_id", imageId,
                "overwrite", true,
                "resource_type", "image"
        );

        File tmpFile = new File(imageId);
        Files.write(tmpFile.toPath(), multipartFile.getBytes());
        cloudinary.uploader().upload(tmpFile, params);
        Files.delete(tmpFile.toPath());

        return String.format("https://res.cloudinary.com/diffdysmk/image/upload/" + imageId + "." + getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));

    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
