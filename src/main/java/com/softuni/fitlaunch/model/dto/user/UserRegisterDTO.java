package com.softuni.fitlaunch.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDTO {

    @NotBlank(message = "Username should not be null nor empty")
    @Length(min = 2, max = 255, message = "Username should be between 2 and 255 characters long!")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Invalid username")
    private String username;

    @Email(message = "Invalid email!")
    @Length(min = 2, max = 255, message = "Email should be between 2 and 255 characters long!")
    @NotBlank(message = "Email should not be null nor empty")
    private String email;

    @NotBlank(message = "Password should not be null nor empty")
    @Length(min = 5, max = 127, message = "Password should be between 5 and 127 characters long!")
    private String password;

    @NotBlank(message = "Confirm password should not be null nor empty")
    @Length(min = 5, max = 127, message = "Confirm password must be between 5 and 127 characters long!")
    private String confirmPassword;

    @NotBlank(message = "Title should not be null nor empty")
    @Length(min = 4, max = 255, message = "Title should be between 2 and 255 characters long!")
    private String title;
}
