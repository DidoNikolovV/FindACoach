package com.softuni.fitlaunch.model.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileDTO {

    private MultipartFile imgUrl;
    private String username;
    private String email;
    private String membership;
}
