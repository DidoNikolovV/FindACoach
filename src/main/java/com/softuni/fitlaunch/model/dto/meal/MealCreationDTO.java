package com.softuni.fitlaunch.model.dto.meal;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MealCreationDTO {
    private String name;
    private String description;
    private String type;

    private Double calories;

    private MultipartFile image;
}
