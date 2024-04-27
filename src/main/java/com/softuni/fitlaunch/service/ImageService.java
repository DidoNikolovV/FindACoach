package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.ImageEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final FileUpload fileUpload;

    public ImageService(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public ImageEntity createImage(MultipartFile image) {
        String imageUrl = fileUpload.uploadFile(image);

        ImageEntity newImage = new ImageEntity();
        newImage.setTitle(image.getOriginalFilename());
        newImage.setUrl(imageUrl);

        return newImage;
    }
}
