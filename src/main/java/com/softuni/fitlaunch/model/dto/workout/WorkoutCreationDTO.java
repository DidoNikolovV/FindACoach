package com.softuni.fitlaunch.model.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkoutCreationDTO {
    @NotBlank(message = "Name should not be null nor empty")
    @Length(max = 255, message = "Name cannot be more than 255 characters long")
    @Pattern(regexp = "^(?!\\s)[a-zA-Z0-9-* ]+", message = "Invalid workout name")
    private String name;
    @NotNull
    private String level;
    @NotNull(message = "Image should not be null")
    private MultipartFile imgUrl;
}
