package com.softuni.fitlaunch.model.dto.view;

import com.softuni.fitlaunch.validation.anotations.FileAnnotation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class ProfilePictureUploadDTO {

    private Long id;

    @FileAnnotation(contentTypes = {"image/png", "image/jpg"})
    private MultipartFile picture;
}
